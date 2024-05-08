package com.yhs0602.vm

import com.yhs0602.dex.ClassData
import com.yhs0602.dex.ClassDef
import com.yhs0602.dex.CodeItem
import com.yhs0602.dex.TypeId
import java.lang.reflect.Constructor
import kotlin.jvm.internal.Intrinsics

interface MockedClass {
    val classId: TypeId

    fun initializeClass()

    // NewInstance will be effectively a constructor
}

class GeneralMockedClass(
    val clazz: Class<*>
) : MockedClass {
    override val classId: TypeId
        get() = TypeId(clazz.descriptorString())

    override fun initializeClass() {
        // Invoke <clinit> method
        clazz.declaredMethods.find { it.name == "<clinit>" }?.invoke(null)
        // It initializes the static fields
    }

    // Ultimiate mocked methods which will be called actually.
    fun getMethods(): List<MockedMethod> = clazz.methods.map {
        object : MockedMethod {
            override fun execute(
                args: Array<RegisterValue>,
                environment: Environment,
                code: CodeItem,
                isStatic: Boolean
            ): Array<RegisterValue> {
                return invokeMethod(name, args, parameters, isStatic)
            }

            override val classId: TypeId
                get() = this@GeneralMockedClass.classId
            override val parameters: List<TypeId>
                get() = it.parameterTypes.map { TypeId(it.descriptorString()) }
            override val name: String
                get() = it.name

            override fun toString(): String {
                return "MockedMethod(classId=$classId, parameters=$parameters, name=$name)"
            }
        }
    } + clazz.constructors.map {
        object : MockedMethod {
            override fun execute(
                args: Array<RegisterValue>,
                environment: Environment,
                code: CodeItem,
                isStatic: Boolean
            ): Array<RegisterValue> {
                return invokeMethod("<init>", args, parameters, false)
            }

            override val classId: TypeId
                get() = this@GeneralMockedClass.classId
            override val parameters: List<TypeId>
                get() = it.parameterTypes.map { TypeId(it.name) }
            override val name: String
                get() = "<init>"

        }
    }


    // Can only be called as a context of mocked class.
    fun invokeMethod(
        name: String,
        args: Array<RegisterValue>,
        paramType: List<TypeId>,
        isStatic: Boolean
    ): Array<RegisterValue> {
        return try {
            // Check if the method name is <init>
            println("Invoking $name with args $args")
            if (name == "<init>") {
                println("Name is <init>")
                val registerValue = (args[0] as? RegisterValue.ObjectRef)?.value
                    ?: throw IllegalArgumentException("Instance not found")
                when (registerValue) {
                    is MockedInstance -> {
                        // There is no actual backing instance,
                        // Use the class's createInstance method
                        // create the instance
                        try {
                            val droppedArgs = args.drop(1)
                            val instance = clazz.constructors.first {
                                compareConstructorProto(it, droppedArgs, paramType)
                            }.newInstance(*droppedArgs.map { marshalArgument(it) }.toTypedArray())

                            registerValue.value = instance
                        } catch (e: NoSuchElementException) {
                            throw IllegalArgumentException("Constructor not found")
                        }
                    }

                    is DictionaryBackedInstance -> {
                        // It should
                    }

                    else -> {
                        throw IllegalArgumentException("Illegal instance type $registerValue")
                    }
                }
                return arrayOf()
            }
            val instanceValue: Any?
            val adjustedArgs: List<RegisterValue>
            if (isStatic) {
                instanceValue = null
                adjustedArgs = args.toList()
            } else {
                val instance = (args[0] as? RegisterValue.ObjectRef)?.value
                instanceValue = if (instance is MockedInstance) instance.value else {
                    throw IllegalArgumentException("Instance not found: instance: $instance, args: $args")
                }
                adjustedArgs = args.drop(1)
            }
            val method = clazz.methods.first {
                compareMethodProto(it, name, adjustedArgs, paramType)
            }
            // Drop first argument as it is the instance, if it is not static
//            val args = if (!AccessFlags(method.modifiers).isStatic) args.drop(1) else args
            println("Invoking $method ${method.parameterTypes.joinToString(" ") { it.name }} with args $adjustedArgs")
            val argArr = adjustedArgs.map {
                marshalArgument(it)
            }.toTypedArray()
            val declaringClass = method.declaringClass
//            if (!declaringClass.isInstance(instanceValue)) {
//                throw IllegalArgumentException("The provided instance $instanceValue does not match the method's declaring class. $declaringClass")
//            }
            println("2. Invoking $name with args $argArr for object $instanceValue")
            val result = method.invoke(instanceValue, *argArr)
            val resultType = method.returnType
            val unmarshalledResult = unmarshalArgument(result, resultType)
            unmarshalledResult
        } catch (e: NoSuchElementException) {
            throw IllegalArgumentException("Method $name not found", e)
        }
    }

    private fun compareConstructorProto(
        method: Constructor<*>,
        args: List<RegisterValue>,
        paramTypes: List<TypeId>
    ): Boolean {
        // compare method name: it should be the class name
//        if (method.name != "<init>") {
//            println("Method name mismatch: ${method.name} != <init>")
//            return false
//        }
        if (method.parameterCount != paramTypes.size) {
            println("Parameter count mismatch: ${method.parameterCount} != ${paramTypes.size}")
            return false
        }
        println("Args: $args, paramTypes: $paramTypes")
        val methodParameterTypes = method.parameterTypes
        var i = 0
        while (i < args.size) {
            val paramType = methodParameterTypes[i]

            if (!compareProtoType(paramTypes[i], paramType)) {
                println("Parameter type mismatch: ${paramTypes[i]} != $paramType")
                return false
            }
            // 파라미터의 타입과 인자의 타입을 비교하여 일치하지 않으면 false를 반환
            val (result, consumed) = compareArgumentType(args, i, paramType)
            if (!result) {
                println("Argument type mismatch: ${args[i]} != $paramType")
                return false
            }
            i += consumed
        }
        return true
    }

    override fun toString(): String {
        return "GeneralMockedClass(classId=$classId, clazz=$clazz)"
    }
}

sealed class ClassRepresentation {
    class DexClassRepresentation(val classDef: ClassDef, val classData: ClassData?) : ClassRepresentation()
    class MockedClassRepresentation(val mockedClass: GeneralMockedClass) : ClassRepresentation()
}