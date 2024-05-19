package com.yhs0602.vm

import com.yhs0602.dex.TypeId
import com.yhs0602.vm.instance.MockedInstance

data class ExceptionValue(val s: String) :
    RegisterValue.ObjectRef(
        type = Environment.getInstance().getType(TypeId("Ljava/lang/Exception;")),
        value = MockedInstance(Exception::class.java).apply { value = s }
    )
