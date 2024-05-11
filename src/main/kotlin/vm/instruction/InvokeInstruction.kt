package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.*

open class InvokeVirtual(pc: Int, val code: CodeItem) : Instruction._35c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val method = environment.getMethod(code, BBBB)
        val argRegList = arrayOf(C, D, E, F, G)
        val args = Array(A) { memory.registers[argRegList[it]] }
        when (method) {
            is MethodWrapper.Mocked -> {
                memory.returnValue = method.mockedMethod.execute(
                    args,
                    environment,
                    code,
                    false
                )
                return pc + insnLength
            }

            is MethodWrapper.Encoded -> {
                val codeItem = method.encodedMethod.codeItem
                val name = method.encodedMethod.methodId.name
                println("Invoking: $name")
                if (codeItem == null) {
                    memory.exception = ExceptionValue("InvokeVirtual: Method not found")
                    return pc + insnLength
                }
                memory.returnValue = executeMethod(codeItem, environment, args, A, depth + 1)
                return pc + insnLength
            }
        }
    }

    override fun toString(): String {
        val argRegList = arrayOf(C, D, E, F, G)
        val args = Array(A) { argRegList[it] }
        return "InvokeVirtual ($A args: ${args.joinToString(",")})"
    }
}

class InvokeSuper(pc: Int, code: CodeItem) : InvokeVirtual(pc, code) {
    // TODO
    override fun toString(): String {
        val argRegList = arrayOf(C, D, E, F, G)
        val args = Array(A) { argRegList[it] }
        return "InvokeSuper ($A args: ${args.joinToString(",")})"
    }
}

class InvokeDirect(pc: Int, code: CodeItem) : InvokeVirtual(pc, code) {
    // TODO
    override fun toString(): String {
        val argRegList = arrayOf(C, D, E, F, G)
        val args = Array(A) { argRegList[it] }
        return "InvokeDirect ($A args: ${args.joinToString(",")})"
    }
}

class InvokeStatic(pc: Int, code: CodeItem) : InvokeVirtual(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val method = environment.getMethod(code, BBBB)
        val argRegList = arrayOf(C, D, E, F, G)
        val args = Array(A) { memory.registers[argRegList[it]] }
        when (method) {
            is MethodWrapper.Mocked -> {
                memory.returnValue = method.mockedMethod.execute(
                    args,
                    environment,
                    code,
                    true
                )
                return pc + insnLength
            }

            is MethodWrapper.Encoded -> {
                val codeItem = method.encodedMethod.codeItem
                if (codeItem == null) {
                    memory.exception = ExceptionValue("InvokeVirtual: Method not found")
                    return pc + insnLength
                }
                memory.returnValue = executeMethod(codeItem, environment, args, A, depth + 1)
                return pc + insnLength
            }
        }
    }

    override fun toString(): String {
        val argRegList = arrayOf(C, D, E, F, G)
        val args = Array(A) { argRegList[it] }
        return "InvokeStatic ($A args: ${args.joinToString(",")})"
    }
}

class InvokeInterface(pc: Int, code: CodeItem) : InvokeVirtual(pc, code) {
    // TODO
}

open class InvokeVirtualRange(pc: Int, val code: CodeItem) : Instruction._3rc(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val method = environment.getMethod(code, BBBB)
        val registerIndices = (CCCC until CCCC + count).toList()
        val args = Array(count) { memory.registers[registerIndices[it]] }
        when (method) {
            is MethodWrapper.Encoded -> {
                val codeItem = method.encodedMethod.codeItem
                if (codeItem == null) {
                    memory.exception = ExceptionValue("InvokeVirtualRange: Method not found")
                    return pc + insnLength
                }
                memory.returnValue = executeMethod(codeItem, environment, args, count, depth + 1)
                return pc + insnLength
            }

            is MethodWrapper.Mocked -> {
                memory.returnValue = method.mockedMethod.execute(
                    args,
                    environment,
                    code,
                    false
                )
                return pc + insnLength
            }
        }
    }
}

class InvokeSuperRange(pc: Int, code: CodeItem) : InvokeVirtualRange(pc, code) {
    // TODO
}

class InvokeDirectRange(pc: Int, code: CodeItem) : InvokeVirtualRange(pc, code) {
    // TODO
    override fun toString(): String {
        val registerIndices = (CCCC until CCCC + count).toList()
        val args = Array(count) { registerIndices[it] }
        return "InvokeDirectRange ($count args: ${args.joinToString(",")})"
    }
}

class InvokeStaticRange(pc: Int, code: CodeItem) : InvokeVirtualRange(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val method = environment.getMethod(code, BBBB)
        val registerIndices = (CCCC until CCCC + count).toList()
        val args = Array(count) { memory.registers[registerIndices[it]] }
        when (method) {
            is MethodWrapper.Encoded -> {
                val codeItem = method.encodedMethod.codeItem
                if (codeItem == null) {
                    memory.exception = ExceptionValue("InvokeVirtualRange: Method not found")
                    return pc + insnLength
                }
                memory.returnValue = executeMethod(codeItem, environment, args, count, depth + 1)
                return pc + insnLength
            }

            is MethodWrapper.Mocked -> {
                memory.returnValue = method.mockedMethod.execute(
                    args,
                    environment,
                    code,
                    true
                )
                return pc + insnLength
            }
        }
    }
}

class InvokeInterfaceRange(pc: Int, code: CodeItem) : InvokeVirtualRange(pc, code) {
    // TODO
}

class InvokePolymorphic(pc: Int, code: CodeItem) : Instruction._45cc(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        return pc + insnLength
    }
}

class InvokePolymorphicRange(pc: Int, code: CodeItem) : Instruction._4rcc(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        return pc + insnLength
    }
}

class InvokeCustom(pc: Int, code: CodeItem) : Instruction._35c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        return pc + insnLength
    }
}

class InvokeCustomRange(pc: Int, code: CodeItem) : Instruction._3rc(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        return pc + insnLength
    }
}

class ConstMethodHandle(pc: Int, code: CodeItem) : Instruction._3rc(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        return pc + insnLength
    }
}

class ConstMethodType(pc: Int, code: CodeItem) : Instruction._3rc(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        return pc + insnLength
    }
}



