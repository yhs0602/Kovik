package com.yhs0602.mockedmethod

import com.yhs0602.dex.CodeItem
import com.yhs0602.dex.TypeId
import com.yhs0602.vm.Environment
import com.yhs0602.vm.MockedMethod
import com.yhs0602.vm.RegisterValue

class KotlinJvmInternalIntrinsicsCheckNotNullPointer : MockedMethod {
    override fun execute(args: Array<RegisterValue>, environment: Environment, code: CodeItem, isStatic: Boolean): Array<RegisterValue> {
        val value = args[0]
        if (value is RegisterValue.ObjectRef) {
            if (value.value == null) {
                when (val message = args[1]) {
                    is RegisterValue.StringRef -> {
                        throw NullPointerException(environment.getString(code, message.index))
                    }

                    is RegisterValue.ObjectRef -> {
                        throw NullPointerException(message.unwrapString())
                    }

                    else -> {
                        println("Unsupported message type $message")
                        throw NullPointerException()
                    }
                }
            }
        }
        return arrayOf()
    }

    override val classId: TypeId
        get() = TypeId("Lkotlin/jvm/internal/Intrinsics;")
    override val parameters: List<TypeId>
        get() = listOf(TypeId("Ljava/lang/Object;"), TypeId("Ljava/lang/String;"))
    override val name: String
        get() = "checkNotNullParameter"
}