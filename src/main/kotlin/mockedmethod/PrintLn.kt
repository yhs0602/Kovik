package com.yhs0602.mockedmethod

import com.yhs0602.dex.CodeItem
import com.yhs0602.dex.TypeId
import com.yhs0602.vm.Environment
import com.yhs0602.vm.MockedInstance
import com.yhs0602.vm.MockedMethod
import com.yhs0602.vm.RegisterValue

class PrintLn : MockedMethod {
    override fun execute(args: Array<RegisterValue>, environment: Environment, code: CodeItem, isStatic: Boolean): Array<RegisterValue> {
        val value = args[0]
        when (value) {
            is RegisterValue.ObjectRef -> {
                when (value.value) {
                    is MockedInstance -> {
                        val stringValue = value.value.value.toString()
                        println(stringValue)
                    }

                    null -> {
                        println("null")
                    }

                    else -> {
                        println(value.value)
                    }
                }
            }

            is RegisterValue.ArrayRef -> {
                println("[" + value.values.joinToString(",") + "]")
            }

            is RegisterValue.ClassRef -> {
                println("ClassRef: ${value.index}")
            }

            is RegisterValue.Int -> {
                println(value.value)
            }

            is RegisterValue.StringRef -> {
                val string = environment.getString(code, value.index)
                println(string)
            }
        }

        return arrayOf()
    }

    override val classId: TypeId
        get() = TypeId("Ljava/io/PrintStream;")
    override val parameters: List<TypeId>
        get() = listOf(TypeId("Ljava/lang/Object;"))
    override val name: String
        get() = "println"
}