package com.yhs0602.mockedmethod

import com.yhs0602.dex.CodeItem
import com.yhs0602.dex.TypeId
import com.yhs0602.vm.Environment
import com.yhs0602.vm.MockedInstance
import com.yhs0602.vm.MockedMethod
import com.yhs0602.vm.RegisterValue

class StringBuilderInit : MockedMethod {

    override fun execute(args: Array<RegisterValue>, environment: Environment, code: CodeItem, isStatic: Boolean): Array<RegisterValue> {
        return arrayOf(
//            RegisterValue.ObjectRef(
//                typeId = TypeId("Ljava/lang/StringBuilder;"),
//                value = MockedInstance(StringBuilder()),
//            )
        )
    }

    override val classId: TypeId
        get() = TypeId("Ljava/lang/StringBuilder;")
    override val parameters: List<TypeId>
        get() = emptyList()
    override val name: String
        get() = "<init>"
}