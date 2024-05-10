package com.yhs0602.vm

import com.yhs0602.dex.TypeId

data class ExceptionValue(val s: String) :
    RegisterValue.ObjectRef(
        typeId = TypeId("Ljava/lang/Exception;"),
        value = MockedInstance(Exception::class.java).apply { value = s }
    )
