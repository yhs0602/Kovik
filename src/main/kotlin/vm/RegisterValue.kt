package com.yhs0602.vm

import com.yhs0602.dex.CodeItem
import com.yhs0602.dex.TypeId

sealed class RegisterValue {
    data class Int(val value: kotlin.Int) : RegisterValue() {
        override fun toString(): String {
            return "Int($value)"
        }
    }
    data class StringRef(val index: kotlin.Int) : RegisterValue() {
        fun toStringObject(code: CodeItem, environment: Environment): ObjectRef {
            val stringValue = environment.getString(code, index)
            return ObjectRef(TypeId("Ljava/lang/String;"), MockedInstance(stringValue))
        }
    }

    data class ClassRef(val index: kotlin.Int) : RegisterValue()
    data class ArrayRef(val typeId: TypeId, val length: kotlin.Int, val values: Array<RegisterValue>) : RegisterValue() {
        override fun toString(): String {
            return "ArrayRef(typeId=${typeId.descriptor}, length=$length, values=${values.contentToString()})"
        }
    }
    open class ObjectRef(val typeId: TypeId, val value: Instance?) : RegisterValue() {
        override fun toString(): String {
            return "ObjectRef(typeId=$typeId, value=$value)"
        }

        fun unwrapString(): String {
            return (value as? MockedInstance)?.value as? String ?: toString()
        }
    }
}