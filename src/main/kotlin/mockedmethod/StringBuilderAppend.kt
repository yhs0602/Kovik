package com.yhs0602.mockedmethod

import com.yhs0602.dex.CodeItem
import com.yhs0602.dex.TypeId
import com.yhs0602.vm.Environment
import com.yhs0602.vm.MockedInstance
import com.yhs0602.vm.MockedMethod
import com.yhs0602.vm.RegisterValue

class StringBuilderAppend : MockedMethod {
    override fun execute(args: Array<RegisterValue>, environment: Environment, code: CodeItem, isStatic: Boolean): Array<RegisterValue> {
        val stringBuilder = args[0] as RegisterValue.ObjectRef
        val string = args[1] as RegisterValue.StringRef
        val stringBuilderValue = stringBuilder.value
        val stringValue = environment.getString(code, string.index)
        return arrayOf()
//        val stringObjectValue = RegisterValue.ObjectRef(TypeId("Ljava/lang.String;"), MockedInstance(stringValue))
//        if (stringBuilderValue !is MockedInstance) {
//            throw IllegalArgumentException("Unsupported instance type")
//        }
//        return arrayOf(stringBuilderValue.invokeMethod("append", listOf(stringObjectValue), parameters))
    }

    override val classId: TypeId
        get() = TypeId("Ljava/lang/StringBuilder;")
    override val parameters: List<TypeId>
        get() = listOf(TypeId("Ljava/lang/String;"))
    override val name: String
        get() = "append"
}