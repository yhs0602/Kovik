package com.yhs0602.vm

import com.yhs0602.dex.EncodedField
import com.yhs0602.dex.TypeId

interface Instance {
    fun getField(idx: Int): Array<RegisterValue>?

    fun setField(idx: Int, value: Array<RegisterValue>)

    fun invokeMethod(name: String, args: List<RegisterValue>): RegisterValue
}

class DictionaryBackedInstance(val fields: List<EncodedField>) : Instance {
    private val fieldValues: MutableMap<Int, Array<RegisterValue>> = List(fields.size) { idx ->
        idx to arrayOf<RegisterValue>(RegisterValue.Int(0))
    }.toMap().toMutableMap()

    override fun getField(idx: Int): Array<RegisterValue>? {
        return fieldValues[idx]
    }

    override fun setField(idx: Int, value: Array<RegisterValue>) {
        fieldValues[idx] = value
    }

    override fun invokeMethod(name: String, args: List<RegisterValue>): RegisterValue {
        return when (name) {
            "toString" -> RegisterValue.StringRef(0)
            else -> throw IllegalArgumentException("Method $name not found")
        }
    }
}

class MockedInstance(val value: Any) : Instance {
    private val clazz = value.javaClass

    override fun getField(idx: Int): Array<RegisterValue> {
        return try {
            val field = clazz.fields[idx]
            val fieldValue = field.get(value)
            when (field.type) {
                Int::class.java -> arrayOf(RegisterValue.Int(fieldValue as Int))
                String::class.java -> arrayOf(RegisterValue.StringRef(0))
                Long::class.java -> {
                    val longValue = fieldValue as Long
                    arrayOf(RegisterValue.Int(longValue.toInt()), RegisterValue.Int((longValue shr 32).toInt()))
                }

                else -> throw IllegalArgumentException("Unsupported field type ${field.type}")
            }
        } catch (e: IndexOutOfBoundsException) {
            throw IllegalArgumentException("Field $idx not found")
        }
    }

    override fun setField(idx: Int, value: Array<RegisterValue>) {
        try {
            val field = clazz.fields[idx]
            val fieldValue = when (field.type) {
                Int::class.java -> (value[0] as RegisterValue.Int).value
                String::class.java -> value[0].toString()
                Long::class.java -> {
                    val low = (value[0] as RegisterValue.Int).value.toLong()
                    val high = (value[1] as RegisterValue.Int).value.toLong()
                    low or (high shl 32)
                }

                else -> throw IllegalArgumentException("Unsupported field type ${field.type}")
            }
            field.set(value, fieldValue)
        } catch (e: IndexOutOfBoundsException) {
            throw IllegalArgumentException("Field $idx not found")
        }
    }

    override fun invokeMethod(name: String, args: List<RegisterValue>): RegisterValue {
        return try {
            val method = clazz.methods.first {
                compareMethodProto(it, name, args)
            }
            // Drop first argument as it is the instance, if it is not static
//            val args = if (!AccessFlags(method.modifiers).isStatic) args.drop(1) else args
            println("Invoking $method ${method.parameterTypes.joinToString(" ") { it.name }} with args $args")
            val argArr = args.map {
                marshalArgument(it) as String
            }.toTypedArray()
            println("2. Invoking $name with args $argArr for object $value")
            val result = method.invoke(value, *argArr)
            val resultType = method.returnType
            val unmarshalledResult = unmarshalArgument(result, resultType)
            unmarshalledResult
        } catch (e: NoSuchElementException) {
            throw IllegalArgumentException("Method $name not found")
        }
    }
}

// TODO: Implement more strictly
fun compareMethodProto(method: java.lang.reflect.Method, name: String, args: List<RegisterValue>): Boolean {
    if (method.name != name)
        return false
    if (method.parameterCount != args.size)
        return false
    val methodParameterTypes = method.parameterTypes

    var i = 0
    while (i < args.size) {
        val paramType = methodParameterTypes[i]

        // 파라미터의 타입과 인자의 타입을 비교하여 일치하지 않으면 false를 반환
        val (result, consumed) = compareArgumentType(args, i, paramType)
        if (!result)
            return false
        i += consumed
    }
    return true
}

fun compareArgumentType(args: List<RegisterValue>, idx: Int, paramType: Class<*>): Pair<Boolean, Int> {
    val arg = args[idx]
    return when {
        paramType == Int::class.java && arg is RegisterValue.Int -> true to 1
        paramType == String::class.java && arg is RegisterValue.StringRef -> true to 1
        paramType == Long::class.java && arg is RegisterValue.Int -> {
            if (args.size <= idx + 1) {
                false to 0
            }
            val nextArg = args[idx + 1]
            if (nextArg is RegisterValue.Int) {
                true to 2
            } else {
                false to 0
            }
        }

        paramType == Float::class.java && arg is RegisterValue.Int -> true to 1
        paramType == Double::class.java && arg is RegisterValue.Int -> {
            if (args.size <= idx + 1) {
                false to 0
            }
            val nextArg = args[idx + 1]
            if (nextArg is RegisterValue.Int) {
                true to 2
            } else {
                false to 0
            }
        }

        paramType == Boolean::class.java && arg is RegisterValue.Int -> true to 1
        paramType == Char::class.java && arg is RegisterValue.Int -> true to 1
        paramType == Byte::class.java && arg is RegisterValue.Int -> true to 1
        paramType == Short::class.java && arg is RegisterValue.Int -> true to 1
        paramType.isArray && arg is RegisterValue.ArrayRef -> {
            if (args.size <= idx + 1) {
                false to 0
            }
            val componentType = paramType.componentType
            val result = arg.values.all { compareArgumentType(listOf(it), 0, componentType).first }
            result to arg.values.size
        }

        paramType == Object::class.java && arg is RegisterValue.ObjectRef -> true to 1
        else -> false to 0
    }
}

fun marshalArgument(registerValue: RegisterValue): Any? {
    return when (registerValue) {
        is RegisterValue.Int -> registerValue.value
        is RegisterValue.StringRef -> java.lang.String("string")
        is RegisterValue.ClassRef -> {
            "class"
        }

        is RegisterValue.ArrayRef -> {
            registerValue.values.map { marshalArgument(it) }
        }

        is RegisterValue.ObjectRef -> {
            when (registerValue.value) {
                is MockedInstance -> registerValue.value.value
                is Instance -> registerValue.value
                else -> null
            }
        }
    }
}


fun unmarshalArgument(value: Any, returnType: Class<*>): RegisterValue {
    return when (value) {
        is Int -> RegisterValue.Int(value)
        is String -> RegisterValue.ObjectRef(TypeId("Ljava/lang/String;"), MockedInstance(value))
        is Array<*> -> {
            val values = value.map {
                unmarshalArgument(it!!, returnType.componentType!!)
            }
            RegisterValue.ArrayRef(TypeId(returnType.componentType!!.typeName), value.size, values.toTypedArray())
        }

        else -> RegisterValue.ObjectRef(TypeId(value::class.javaObjectType.typeName), MockedInstance(value))
    }
}