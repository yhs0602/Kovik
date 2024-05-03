package com.yhs0602.mockedmethod

import com.yhs0602.dex.CodeItem
import com.yhs0602.dex.TypeId
import com.yhs0602.vm.Environment
import com.yhs0602.vm.MockedMethod
import com.yhs0602.vm.RegisterValue

class ObjectInit : MockedMethod {
    override fun execute(args: Array<RegisterValue>, environment: Environment, code: CodeItem): Array<RegisterValue> {
        return emptyArray()
    }

    override val classId: TypeId
        get() = TypeId("Ljava/lang/Object;")
    override val parameters: List<TypeId>
        get() = emptyList()
    override val name: String
        get() = "<init>"
}