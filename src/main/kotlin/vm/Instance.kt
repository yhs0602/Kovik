package com.yhs0602.vm

import com.yhs0602.dex.EncodedField

class Instance(val fields: List<EncodedField>) {
    private val fieldValues: MutableMap<Int, Array<RegisterValue>> = List(fields.size) { idx ->
        idx to arrayOf<RegisterValue>(RegisterValue.Int(0))
    }.toMap().toMutableMap()

    fun getField(idx: Int): Array<RegisterValue>? {
        return fieldValues[idx]
    }

    fun setField(idx: Int, value: Array<RegisterValue>) {
        fieldValues[idx] = value
    }

    fun invokeMethod(name: String, args: List<RegisterValue>): RegisterValue {
        return when (name) {
            "toString" -> RegisterValue.StringRef(0)
            else -> throw IllegalArgumentException("Method $name not found")
        }
    }
}