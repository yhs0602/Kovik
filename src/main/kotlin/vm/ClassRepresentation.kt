package com.yhs0602.vm

import com.yhs0602.dex.ClassData
import com.yhs0602.dex.ClassDef
import com.yhs0602.dex.CodeItem
import com.yhs0602.dex.TypeId
import com.yhs0602.vm.instance.*
import net.sf.cglib.proxy.Enhancer
import java.lang.reflect.Constructor


class GeneralMockedClass(
    val clazz: Class<*>
) {
    val classId: TypeId
        get() = TypeId(clazz.descriptorString())

    fun getMethods(): List<MockedMethod> = clazz.methods.map {
        object : MockedMethod {
            override fun execute(
                args: Array<out RegisterValue>,
                environment: Environment,
                code: CodeItem,
                isStatic: Boolean
            ): Array<RegisterValue> {
                return invokeMethod(environment, code, name, args, parameters, isStatic)
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
                args: Array<out RegisterValue>,
                environment: Environment,
                code: CodeItem,
                isStatic: Boolean
            ): Array<RegisterValue> {
                return invokeMethod(environment, code, "<init>", args, parameters, false)
            }

            override val classId: TypeId
                get() = this@GeneralMockedClass.classId
            override val parameters: List<TypeId>
                get() = it.parameterTypes.map { TypeId(it.descriptorString()) }
            override val name: String
                get() = "<init>"

            override fun toString(): String {
                return "MockedConstructor(classId=$classId, parameters=$parameters, name=$name)"
            }
        }
    }


    // Can only be called as a context of mocked class.
    // including <init>
    fun invokeMethod(
        environment: Environment,
        code: CodeItem,
        name: String,
        args: Array<out RegisterValue>,
        paramType: List<TypeId>,
        isStatic: Boolean
    ): Array<RegisterValue> {
        return try {
            // Check if the method name is <init>
//            println("Invoking $name with args $args")
            if (name == "<init>") {
//                println("Name is <init>")
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
                            }.run {
                                newInstance(*marshalArguments(environment, code, droppedArgs, parameterTypes))
                            }
                            registerValue.value = instance
                        } catch (e: NoSuchElementException) {
                            throw IllegalArgumentException("Constructor not found")
                        }
                    }

                    is DictionaryBackedInstance -> {
                        // new-instance v0, dexClass
                        // invoke-super v0, mockedClass-><init>
                        // In this example:
                        // class A(s: String): File(s) {}
                        // class B(s: String): A(s) {}
                        // class C(s: String): B(s) {}
                        // new-instance v0, C
                        // invoke-virtual v0, C-><init>
                        // New frame start for C-><init>
                        // Usually it first calls the superclass's constructor
                        // invoke-super v0, B-><init>
                        // New frame start for B-><init>
                        // invoke-super v0, A-><init>
                        // And the control flows here. we have A's instance,
                        // And the class information is precalculated in DictionaryBackedInstance's init
                        val backingSuperClass = registerValue.backingSuperClass
                        val interfaces = registerValue.interfaces
                        if (backingSuperClass != null) {
                            try {
                                val droppedArgs = args.drop(1)
                                val instance = backingSuperClass.constructors.first {
                                    compareConstructorProto(it, droppedArgs, paramType)
                                }.run {
                                    // Use CGLib to create a proxy instance
                                    println("Creating proxy object for ${backingSuperClass} with interface ${interfaces.joinToString()}")
                                    val enhancer = Enhancer()
                                    enhancer.setSuperclass(backingSuperClass)
                                    if (interfaces.isNotEmpty())
                                        enhancer.setInterfaces(interfaces.toTypedArray())
                                    enhancer.setCallback(registerValue)
                                    registerValue.backingSuperInstance = enhancer.create(
                                        parameterTypes,
                                        marshalArguments(environment, code, droppedArgs, parameterTypes)
                                    )
//                                    registerValue.backingOriginalSuperInstance = this.newInstance(
//                                        *marshalArguments(environment, code, droppedArgs, parameterTypes)
//                                    )
                                }
                            } catch (e: NoSuchElementException) {
                                throw IllegalArgumentException("Constructor not found")
                            }
                        } else {
                            // throw IllegalArgumentException("Backing super class not found")
                            // DO NOTHING
                            System.err.println("Backing super class not found")
                        }
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
                    // We have to marshal the instance
                    val (marshalledInstance, _) = marshalArgument(environment, code, args.toList(), 0, clazz)
                    marshalledInstance
//                    throw IllegalArgumentException("Instance not found: instance: $instance, args: $args")
                }
                adjustedArgs = args.drop(1)
            }
            val method = clazz.methods.first {
                compareMethodProto(it, name, adjustedArgs, paramType)
            }
            // Drop first argument as it is the instance, if it is not static
//            val args = if (!AccessFlags(method.modifiers).isStatic) args.drop(1) else args
            println("Invoking $method ${method.parameterTypes.joinToString(" ") { it.name }} with args $adjustedArgs")
            val argArr = marshalArguments(environment, code, adjustedArgs, method.parameterTypes)
            if (instanceValue != null) {
                val declaringClass = method.declaringClass
                if (!declaringClass.isInstance(instanceValue)) {
                    throw IllegalArgumentException("The provided instance $instanceValue does not match the method's declaring class. $declaringClass")
                }
            }
            println("2. Invoking $name with args $argArr for object ${instanceValue?.javaClass?.simpleName}")
            // Check if all are Comparable
            if (argArr.isNotEmpty()) {
                val arrarg = argArr[0]
                if (arrarg is Array<*>) {
                    for(arrargg in arrarg) {
                        if (arrargg !is Comparable<*>) {
                            println("Not comparable: ${arrargg}")
                        } else {
                            println("Comparable: ${arrargg}")
                        }
                    }
                } else if (arrarg != null) {
                    println("Not an array: ${arrarg}; ${arrarg::class.java}")
                }
            }
            val result = method.invoke(instanceValue, *argArr)
            val resultType = method.returnType
            val unmarshalledResult = unmarshalArgument(result, resultType)
            unmarshalledResult
        } catch (e: NoSuchElementException) {
            throw IllegalArgumentException("Method $name not found: ${args.contentToString()} $paramType $isStatic", e)
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
//            println("Parameter count mismatch: ${method.parameterCount} != ${paramTypes.size}")
            return false
        }
//        println("Args: $args, paramTypes: $paramTypes")
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