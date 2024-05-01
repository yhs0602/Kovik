package com.yhs0602.vm

sealed class RegisterValue {
    data class Int(val value: kotlin.Int) : RegisterValue()
    data class StringRef(val index: kotlin.Int) : RegisterValue()
    data class ClassRef(val index: kotlin.Int) : RegisterValue()
    data class ArrayRef(val value: kotlin.Array<RegisterValue>) : RegisterValue()
}