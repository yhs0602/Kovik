package com.yhs0602.mockedmethod


import com.yhs0602.dex.CodeItem
import com.yhs0602.dex.TypeId
import com.yhs0602.vm.Environment
import com.yhs0602.vm.MockedInstance
import com.yhs0602.vm.MockedMethod
import com.yhs0602.vm.RegisterValue

class StringBuilderToString : MockedMethod {
    override fun execute(args: Array<RegisterValue>, environment: Environment, code: CodeItem): Array<RegisterValue> {
        val stringBuilder = args[0] as RegisterValue.ObjectRef
        val stringBuilderValue = stringBuilder.value
        if (stringBuilderValue !is MockedInstance) {
            throw IllegalArgumentException("Unsupported instance type")
        }
        return arrayOf(stringBuilderValue.invokeMethod(name, listOf(), parameters))
    }

    override val classId: TypeId
        get() = TypeId("Ljava/lang/StringBuilder;")
    override val parameters: List<TypeId>
        get() = listOf()
    override val name: String
        get() = "toString"
}