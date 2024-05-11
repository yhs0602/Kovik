package com.yhs0602.vm

import com.yhs0602.dex.CodeItem
import com.yhs0602.dex.EncodedMethod
import com.yhs0602.dex.TypeId


interface MockedMethod {
    fun execute(
        args: Array<out RegisterValue>,
        environment: Environment,
        code: CodeItem,
        isStatic: Boolean
    ): Array<RegisterValue>

    val classId: TypeId
    val parameters: List<TypeId>
    val name: String
}

sealed class MethodWrapper {
    abstract fun execute(
        args: Array<out RegisterValue>,
        environment: Environment,
        code: CodeItem,
        isStatic: Boolean,
        depth: Int
    ): Array<RegisterValue>

    class Encoded(val encodedMethod: EncodedMethod) : MethodWrapper() {
        override fun execute(
            args: Array<out RegisterValue>,
            environment: Environment,
            code: CodeItem,
            isStatic: Boolean,
            depth: Int
        ): Array<RegisterValue> {
            val codeItem = encodedMethod.codeItem
                ?: throw IllegalStateException("Method ${encodedMethod.methodId} is abstract")
            return executeMethod(
                codeItem,
                environment,
                args,
                args.size,
                depth = depth + 1
            )
        }
    }

    class Mocked(val mockedMethod: MockedMethod) : MethodWrapper() {
        override fun execute(
            args: Array<out RegisterValue>,
            environment: Environment,
            code: CodeItem,
            isStatic: Boolean,
            depth: Int
        ): Array<RegisterValue> = mockedMethod.execute(
            args,
            environment,
            code,
            isStatic,
        )
    }
}