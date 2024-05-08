package com.yhs0602.vm

import com.yhs0602.dex.EncodedField
import com.yhs0602.dex.TypeId

interface Instance {
    fun getField(idx: Int): Array<RegisterValue>?

    fun setField(idx: Int, value: Array<RegisterValue>)

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
}

class MockedInstance(val clazz: Class<*>) : Instance {
    lateinit var value: Any

    override fun getField(idx: Int): Array<RegisterValue> {
        return try {
            val field = clazz.fields[idx]
            val fieldValue = field.get(value)
            unmarshalArgument(fieldValue, field.type)
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

    override fun toString(): String {
        if (!this::value.isInitialized)
            return "MockedInstance(NotInitialized)"
        return "MockedInstance($value)"
    }
}

// TODO: Implement more strictly
fun compareMethodProto(
    method: java.lang.reflect.Method,
    name: String,
    args: List<RegisterValue>,
    paramTypes: List<TypeId>
): Boolean {
    if (method.name != name)
        return false
    // Check instance method or static
    if (method.parameterCount != paramTypes.size)
        return false
    val methodParameterTypes = method.parameterTypes

    var i = 0
    while (i < args.size) {
        val paramType = methodParameterTypes[i]

        if (!compareProtoType(paramTypes[i], paramType))
            return false
        // 파라미터의 타입과 인자의 타입을 비교하여 일치하지 않으면 false를 반환
        val (result, consumed) = compareArgumentType(args, i, paramType)
        if (!result)
            return false
        i += consumed
    }
    return true
}

// TODO: Implement polymorphism
fun compareProtoType(typeId: TypeId, paramType: Class<*>): Boolean {
    return when {
        typeId.descriptor == "I" && paramType == Int::class.java -> true
        typeId.descriptor == "Ljava/lang/String;" && paramType == String::class.java -> true
        typeId.descriptor == "J" && paramType == Long::class.java -> true
        typeId.descriptor == "F" && paramType == Float::class.java -> true
        typeId.descriptor == "D" && paramType == Double::class.java -> true
        typeId.descriptor == "Z" && paramType == Boolean::class.java -> true
        typeId.descriptor == "C" && paramType == Char::class.java -> true
        typeId.descriptor == "B" && paramType == Byte::class.java -> true
        typeId.descriptor == "S" && paramType == Short::class.java -> true
        typeId.descriptor == "Ljava/lang/Object;" && paramType == Object::class.java -> true
        else -> false
    }
}

fun compareArgumentType(args: List<RegisterValue>, idx: Int, paramType: Class<*>): Pair<Boolean, Int> {
    val arg = args[idx]
    return when {
        paramType == Int::class.java && arg is RegisterValue.Int -> true to 1
        paramType == String::class.java && arg is RegisterValue.StringRef -> true to 1
        paramType == String::class.java && arg is RegisterValue.ObjectRef -> {
            if (arg.value is MockedInstance && arg.value.value is String) {
                true to 1
            } else {
                false to 0
            }
        }

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


fun unmarshalArgument(value: Any, returnType: Class<*>): Array<RegisterValue> {
    return when (value) {
        is Int -> arrayOf(RegisterValue.Int(value))
        is Long -> {
            val low = value.toInt()
            val high = (value shr 32).toInt()
            arrayOf(RegisterValue.Int(low), RegisterValue.Int(high))
        }

        is String -> arrayOf(
            RegisterValue.ObjectRef(
                TypeId("Ljava/lang/String;"),
                MockedInstance(String::class.java).apply {
                    this.value = value
                })
        )

        is Array<*> -> {
            val values = value.flatMap {
                unmarshalArgument(it!!, returnType.componentType!!).toList()
            }
            arrayOf(
                RegisterValue.ArrayRef(
                    TypeId(returnType.componentType!!.typeName),
                    value.size,
                    values.toTypedArray()
                )
            )
        }

        else -> arrayOf(
            RegisterValue.ObjectRef(
                TypeId(value::class.javaObjectType.typeName),
                MockedInstance(value::class.javaObjectType).apply {
                    this.value = value
                })
        )
    }
}