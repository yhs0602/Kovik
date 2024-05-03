package com.yhs0602.vm

import com.yhs0602.dex.CodeItem
import com.yhs0602.dex.EncodedMethod
import com.yhs0602.dex.TypeId


interface MockedMethod {
    fun execute(args: Array<RegisterValue>, environment: Environment, code: CodeItem): Array<RegisterValue>

    val classId: TypeId
    val parameters: List<TypeId>
    val name: String
}

sealed class MethodWrapper {
    class Encoded(val encodedMethod: EncodedMethod) : MethodWrapper()
    class Mocked(val mockedMethod: MockedMethod) : MethodWrapper()
}