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
            val method = clazz.methods.first { it.name == name }
            val result = method.invoke(value, *args.map {
                it
            }.toTypedArray())
            when (method.returnType) {
                Int::class.java -> RegisterValue.Int(result as Int)
                String::class.java -> RegisterValue.ObjectRef(TypeId("Ljava/lang/String;"), MockedInstance(result))
                else -> throw IllegalArgumentException("Unsupported return type ${method.returnType}")
            }
        } catch (e: NoSuchElementException) {
            throw IllegalArgumentException("Method $name not found")
        }
    }
}

fun marshalArgument(registerValue: RegisterValue): Any? {
    return when (registerValue) {
        is RegisterValue.Int -> registerValue.value
        is RegisterValue.StringRef -> "string"
        is RegisterValue.ClassRef -> {
            "class"
        }

        is RegisterValue.ArrayRef -> {
            registerValue.values.map { marshalArgument(it) }
        }

        is RegisterValue.ObjectRef -> {
            when(registerValue.value) {
                is MockedInstance -> registerValue.value.value
                is Instance -> registerValue.value
                else -> null
            }
        }
    }
}
