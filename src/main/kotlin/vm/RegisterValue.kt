package com.yhs0602.vm

import com.yhs0602.dex.TypeId

sealed class RegisterValue {
    data class Int(val value: kotlin.Int) : RegisterValue()
    data class StringRef(val index: kotlin.Int) : RegisterValue()
    data class ClassRef(val index: kotlin.Int) : RegisterValue()
    data class ArrayRef(val typeId: TypeId, val length: kotlin.Int, val values: Array<RegisterValue>) : RegisterValue()
    open class ObjectRef(val typeDescriptor: String, val value: Any) : RegisterValue()
}