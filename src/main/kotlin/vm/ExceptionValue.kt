package com.yhs0602.vm

import com.yhs0602.dex.TypeId

class ExceptionValue(s: String) :
    RegisterValue.ObjectRef(typeId = TypeId("Ljava/lang/Throwable;"), value = Instance(emptyList()))
