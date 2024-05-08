package com.yhs0602.vm

import com.yhs0602.dex.ClassData
import com.yhs0602.dex.ClassDef
import com.yhs0602.dex.CodeItem
import com.yhs0602.dex.TypeId
import java.lang.reflect.Constructor

interface MockedClass {
    val classId: TypeId

    fun initializeClass()

    // NewInstance will be effectively a constructor
}

class GeneralMockedClass(
    val clazz: Class<*>
) : MockedClass {
    override val classId: TypeId
        get() = TypeId("L" + clazz.name.replace(".", "/"))

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
                get() = TypeId(clazz.name.replace(".", "/"))
            override val parameters: List<TypeId>
                get() = it.parameterTypes.map { TypeId(it.name) }
            override val name: String
                get() = it.name
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
            if (name == "<init>") {
                val registerValue = (args[0] as? RegisterValue.ObjectRef)?.value
                    ?: throw IllegalArgumentException("Instance not found")
                when (registerValue) {
                    is MockedInstance -> {
                        // There is no actual backing instance,
                        // Use the class's createInstance method
                        // create the instance
                        val instance = clazz.constructors.first {
                            compareConstructorProto(it, name, args.toList(), paramType)
                        }.newInstance(*args.map { marshalArgument(it) }.toTypedArray())
                        registerValue.value = instance
                    }

                    is DictionaryBackedInstance -> {
                        throw IllegalArgumentException("DictionaryBackedInstance is not supported")
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
                val instnace = (args[0] as? RegisterValue.ObjectRef)?.value
                instanceValue = if (instnace is MockedInstance) instnace.value else {
                    throw IllegalArgumentException("Instance not found")
                }
                adjustedArgs = args.drop(1)
            }
            val method = clazz.methods.first {
                compareMethodProto(it, name, adjustedArgs, paramType)
            }
            // Drop first argument as it is the instance, if it is not static
//            val args = if (!AccessFlags(method.modifiers).isStatic) args.drop(1) else args
            println("Invoking $method ${method.parameterTypes.joinToString(" ") { it.name }} with args $args")
            val argArr = args.map {
                marshalArgument(it)
            }.toTypedArray()
            println("2. Invoking $name with args $argArr for object $instanceValue")
            val result = method.invoke(instanceValue, *argArr)
            val resultType = method.returnType
            val unmarshalledResult = unmarshalArgument(result, resultType)
            unmarshalledResult
        } catch (e: NoSuchElementException) {
            throw IllegalArgumentException("Method $name not found")
        }
    }

    private fun compareConstructorProto(
        it: Constructor<*>?,
        name: String,
        toList: List<RegisterValue>,
        paramType: List<TypeId>
    ): Boolean {
        // compare method name
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "GeneralMockedClass(classId=$classId, clazz=$clazz)"
    }
}

sealed class ClassRepresentation {
    class DexClassRepresentation(val classDef: ClassDef, val classData: ClassData?) : ClassRepresentation()
    class MockedClassRepresentation(val mockedClass: GeneralMockedClass) : ClassRepresentation()
}