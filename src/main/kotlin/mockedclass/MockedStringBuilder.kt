package com.yhs0602.mockedclass

import com.yhs0602.dex.TypeId
import com.yhs0602.vm.MockedClass

class MockedStringBuilder : MockedClass {

    override fun createInstance(): Any {
        return StringBuilder()
    }

    override val classId: TypeId
        get() = TypeId("Ljava/lang/StringBuilder;")
}