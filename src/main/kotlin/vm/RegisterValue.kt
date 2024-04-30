package com.yhs0602.vm

import com.yhs0602.dex.TypeId

sealed class RegisterValue {
    data class Int(val value: kotlin.Int) : RegisterValue()
    data class StringRef(val value: String) : RegisterValue()
    data class ClassRef(val value: TypeId) : RegisterValue()
    data class ArrayRef(val value: kotlin.Array<RegisterValue>) : RegisterValue()
}