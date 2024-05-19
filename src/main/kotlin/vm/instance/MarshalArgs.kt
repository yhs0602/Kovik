package com.yhs0602.vm.instance

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Environment
import com.yhs0602.vm.RegisterValue

// to handle wide values correctly, we need to see the whole argument list
// Convert List of registervalues to array of Any?
fun marshalArguments(
    environment: Environment,
    code: CodeItem,
    args: List<RegisterValue>,
    paramTypes: Array<Class<*>>
): Array<Any?> {
    val results = mutableListOf<Any?>()
    var i = 0
    while (i < args.size) {
        val paramType = paramTypes[i]
        val (result, consumed) = marshalArgument(environment, code, args, i, paramType)
        results.add(result)
        i += consumed
    }
    return results.toTypedArray()
}

// Returns the marshalled argument and the number of consumed arguments
fun marshalArgument(
    environment: Environment,
    code: CodeItem,
    args: List<RegisterValue>,
    idx: Int,
    paramType: Class<*>
): Pair<Any?, Int> {
    val arg = args[idx]
    return when {
        (paramType == Int::class.java || paramType == Integer.TYPE) && arg is RegisterValue.Int -> arg.value to 1
        paramType == String::class.java && arg is RegisterValue.StringRef -> environment.getString(code, arg.index) to 1
        paramType == String::class.java && arg is RegisterValue.ObjectRef -> {
            if (arg.value is MockedInstance && arg.value.value is String) {
                arg.value.value to 1
            } else {
                null to 1
            }
        }

        (paramType == Long::class.java || paramType == java.lang.Long.TYPE) && arg is RegisterValue.Int -> {
            if (args.size <= idx + 1) {
                throw IllegalArgumentException("Long argument not found")
            }
            val nextArg = args[idx + 1]
            if (nextArg is RegisterValue.Int) {
                val low = arg.value.toLong() and 0xFFFFFFFF
                val high = nextArg.value.toLong() and 0xFFFFFFFF
                low or (high shl 32) to 2
            } else {
                throw IllegalArgumentException("Long argument not found; second argument is not an integer: $nextArg")
            }
        }

        (paramType == Float::class.java || paramType == java.lang.Float.TYPE) && arg is RegisterValue.Int -> {
            Float.fromBits(arg.value) to 1
        }

        (paramType == Double::class.java || paramType == java.lang.Double.TYPE) && arg is RegisterValue.Int -> {
            if (args.size <= idx + 1) {
                throw IllegalArgumentException("Double argument not found")
            }
            val nextArg = args[idx + 1]
            if (nextArg is RegisterValue.Int) {
                val low = arg.value.toLong()
                val high = nextArg.value.toLong()
                Double.fromBits(low or (high shl 32)) to 2
            } else {
                throw IllegalArgumentException("Double argument not found; second argument is not an integer: $nextArg")
            }
        }

        (paramType == Boolean::class.java || paramType == java.lang.Boolean.TYPE) && arg is RegisterValue.Int -> (arg.value != 0) to 1
        (paramType == Char::class.java || paramType == Character.TYPE) && arg is RegisterValue.Int -> arg.value.toChar() to 1
        (paramType == Byte::class.java || paramType == java.lang.Byte.TYPE) && arg is RegisterValue.Int -> arg.value.toByte() to 1
        (paramType == Short::class.java || paramType == java.lang.Short.TYPE) && arg is RegisterValue.Int -> arg.value.toShort() to 1
        paramType.isArray && arg is RegisterValue.ArrayRef -> {
            // TODO: wide array
//            if (args.size <= idx + 1) {
//                throw IllegalArgumentException("Array argument not found; second argument is not an array: ${args[idx + 1]}")
//            }
            val componentType = paramType.componentType
            val result = arg.values.map {
                // TODO: Wide
                marshalArgument(environment, code, listOf(it), 0, componentType).first
            }.toTypedArray()
            result to 1
        }

        // unify the handling of Object and interface using CGLib and Objenesis
//        paramType.isInterface -> {
//            handleInterface(arg, paramType, args, environment, code)
//        }

        else -> when (arg) {
            is RegisterValue.ObjectRef -> {
                println("Marshalling object reference: $arg")
                when (val theInstance = arg.value) {
                    is MockedInstance -> { // instance of the external class is passed directly
                        theInstance.value to 1
                    }

                    is DictionaryBackedInstance -> {
                        // Dex defined class instance is passed to the method of external class.
                        // We should handle the cases where the required type is finer than Object.
                        // Valid cases:
                        // foo(A a) <- A: This is not the case here, because it is handled in MockedInstance case.
                        // foo(A a) <- B where B extends A. This can happen.
                        // foo(Object a) <- A where A is the Dex defined class.
                        // This case is suspicious, because it is likely that the method can use reflection or
                        // other methods to access the fields of the object.
                        // Also, we may have to mock getClass, hashCode, equals, toString, notify, notifyAll, wait, finalize
                        // I think it will be safer to create a proxy object also for this case.
                        // Checking whether the class is assignable from the required type:
                        // If there is not backing super instance class, it means the direct superclass of the class
                        // is either another Dex defined class or Object.
                        println("Marshalling dictionary backed reference: $arg")
                        val backingSuperInstanceClass = theInstance.backingSuperClass
                        val interfaceClass = theInstance.interfaces.find { paramType.isAssignableFrom(it) }

                        when {
                            interfaceClass != null -> {
                                // super class is Object or Dex defined class
                                println("It implements the required interface: $interfaceClass")
                                // check if any of the interfaces are assignable
                                interfaceClass.cast(theInstance.backingSuperInstance) to 1
                            }

                            backingSuperInstanceClass == null -> {
                                println("Super class is Object or Dex defined class")
                                theInstance.backingSuperInstance to 1
                            }

                            paramType.isAssignableFrom(backingSuperInstanceClass) -> {
                                // It means that the required type is a superclass of the backing super instance class
                                // We need to create a proxy object
                                if (theInstance.backingSuperInstance == null) {
                                    null to 1
                                } else {
                                    theInstance.backingSuperInstance to 1
                                }
                            }

                            else -> {
                                // It means that the required type is not a superclass of the backing super instance class
                                throw IllegalArgumentException("Incompatible types: $paramType and ${theInstance.backingSuperClass}")
                            }
                        }
                    }

                    null -> null to 1
                    is ByteBuddyBackedInstance -> TODO()
                }
            }

            is RegisterValue.StringRef -> {
                environment.getString(code, arg.index) to 1
            }

            is RegisterValue.ClassRef -> {
                // we might use cglib to create the class
                val typeId = environment.getTypeId(code, arg.index)
                // Use new version of loadClass
                environment.loadClass(typeId) to 1
            }

            else -> throw IllegalArgumentException("Cannot marshal object reference: $arg")
        }
    }
}