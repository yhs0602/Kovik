package com.yhs0602.vm

import com.yhs0602.dex.TypeId

data class ExceptionValue(val s: String) :
    RegisterValue.ObjectRef(typeId = TypeId("Ljava/lang/Throwable;"), value = DictionaryBackedInstance(emptyList()))
