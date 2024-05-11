package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.*

open class InvokeVirtual(pc: Int, val code: CodeItem) : Instruction._35c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val argRegList = arrayOf(C, D, E, F, G)
        val args = Array(A) { memory.registers[argRegList[it]] }
        val instance = args.firstOrNull()
        if (instance !is RegisterValue.ObjectRef) {
            memory.exception = ExceptionValue("InvokeVirtual: Not an object reference")
            return pc + insnLength
        }
        val method = environment.getMethod(code, BBBB, instance, direct = false, false)
        when (method) {
            is MethodWrapper.Mocked -> {
                methodName = method.mockedMethod.name
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
                methodName = method.encodedMethod.methodId.name
                println("Invoking: $methodName")
                if (codeItem == null) {
                    memory.exception = ExceptionValue("InvokeVirtual: Method body not found")
                    return pc + insnLength
                }
                memory.returnValue = executeMethod(codeItem, environment, args, A, depth + 1)
                return pc + insnLength
            }
        }
    }

    protected var methodName: String = ""
    override fun toString(): String {
        val argRegList = arrayOf(C, D, E, F, G)
        val args = Array(A) { argRegList[it] }
        return "InvokeVirtual($methodName) ($A args: ${args.joinToString(",")})"
    }
}

class InvokeSuper(pc: Int, code: CodeItem) : InvokeVirtual(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val argRegList = arrayOf(C, D, E, F, G)
        val args = Array(A) { memory.registers[argRegList[it]] }
        val instance = args.firstOrNull()
        if (instance !is RegisterValue.ObjectRef) {
            memory.exception = ExceptionValue("InvokeVirtual: Not an object reference")
            return pc + insnLength
        }
        val method = environment.getMethod(code, BBBB, instance, direct = false, true)
        when (method) {
            is MethodWrapper.Mocked -> {
                methodName = method.mockedMethod.name
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
                methodName = method.encodedMethod.methodId.name
                println("Invoking: $methodName")
                if (codeItem == null) {
                    memory.exception = ExceptionValue("InvokeVirtual: Method body not found")
                    return pc + insnLength
                }
                memory.returnValue = executeMethod(codeItem, environment, args, A, depth + 1)
                return pc + insnLength
            }
        }
    }
    // TODO
    override fun toString(): String {
        val argRegList = arrayOf(C, D, E, F, G)
        val args = Array(A) { argRegList[it] }
        return "InvokeSuper($methodName) ($A args: ${args.joinToString(",") { "v$it" }})"
    }
}

class InvokeDirect(pc: Int, code: CodeItem) : InvokeVirtual(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val argRegList = arrayOf(C, D, E, F, G)
        val args = Array(A) { memory.registers[argRegList[it]] }
        val instance = args.firstOrNull()
        if (instance !is RegisterValue.ObjectRef) {
            memory.exception = ExceptionValue("InvokeVirtual: Not an object reference")
            return pc + insnLength
        }
        val method = environment.getMethod(code, BBBB, instance, direct = true, false)
        when (method) {
            is MethodWrapper.Mocked -> {
                methodName = method.mockedMethod.name
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
                methodName = method.encodedMethod.methodId.name
                println("Invoking: $methodName")
                if (codeItem == null) {
                    memory.exception = ExceptionValue("InvokeVirtual: Method body not found")
                    return pc + insnLength
                }
                memory.returnValue = executeMethod(codeItem, environment, args, A, depth + 1)
                return pc + insnLength
            }
        }
    }
    // TODO
    override fun toString(): String {
        val argRegList = arrayOf(C, D, E, F, G)
        val args = Array(A) { argRegList[it] }
        return "InvokeDirect($methodName) ($A args: ${args.joinToString(",")})"
    }
}

class InvokeStatic(pc: Int, val code: CodeItem) : Instruction._35c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val method = environment.getMethod(code, BBBB, null, direct = true, false)
        val argRegList = arrayOf(C, D, E, F, G)
        val args = Array(A) { memory.registers[argRegList[it]] }
        when (method) {
            is MethodWrapper.Mocked -> {
                methodName = method.mockedMethod.name
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
                    memory.exception = ExceptionValue("InvokeStatic: Method implementation not found")
                    return pc + insnLength
                }
                methodName = method.encodedMethod.methodId.name
                memory.returnValue = executeMethod(codeItem, environment, args, A, depth + 1)
                return pc + insnLength
            }
        }
    }

    private var methodName: String = ""

    override fun toString(): String {
        val argRegList = arrayOf(C, D, E, F, G)
        val args = Array(A) { argRegList[it] }
        return "InvokeStatic($methodName) ($A args: ${args.joinToString(",")})"
    }
}

class InvokeInterface(pc: Int, code: CodeItem) : InvokeVirtual(pc, code) {
    override fun toString(): String {
        val argRegList = arrayOf(C, D, E, F, G)
        val args = Array(A) { argRegList[it] }
        return "InvokeInterface($methodName) ($A args: ${args.joinToString(",")})"
    }
}

open class InvokeVirtualRange(pc: Int, val code: CodeItem) : Instruction._3rc(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val registerIndices = (CCCC until CCCC + count).toList()
        val args = Array(count) { memory.registers[registerIndices[it]] }
        val instance = args.firstOrNull()
        if (instance !is RegisterValue.ObjectRef) {
            memory.exception = ExceptionValue("InvokeVirtual: Not an object reference")
            return pc + insnLength
        }
        val method = environment.getMethod(code, BBBB, instance, direct = false, false)
        when (method) {
            is MethodWrapper.Encoded -> {
                val codeItem = method.encodedMethod.codeItem
                methodName = method.encodedMethod.methodId.name
                if (codeItem == null) {
                    memory.exception = ExceptionValue("InvokeVirtualRange: Method not found")
                    return pc + insnLength
                }
                memory.returnValue = executeMethod(codeItem, environment, args, count, depth + 1)
                return pc + insnLength
            }

            is MethodWrapper.Mocked -> {
                methodName = method.mockedMethod.name
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

    protected var methodName: String = ""

    override fun toString(): String {
        val registerIndices = (CCCC until CCCC + count).toList()
        val args = Array(count) { registerIndices[it] }
        return "InvokeVirtualRange($methodName) ($count args: ${args.joinToString(",")})"

    }
}

class InvokeSuperRange(pc: Int, code: CodeItem) : InvokeVirtualRange(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val registerIndices = (CCCC until CCCC + count).toList()
        val args = Array(count) { memory.registers[registerIndices[it]] }
        val instance = args.firstOrNull()
        if (instance !is RegisterValue.ObjectRef) {
            memory.exception = ExceptionValue("InvokeVirtual: Not an object reference")
            return pc + insnLength
        }
        val method = environment.getMethod(code, BBBB, instance, direct = false, true)
        when (method) {
            is MethodWrapper.Encoded -> {
                val codeItem = method.encodedMethod.codeItem
                methodName = method.encodedMethod.methodId.name
                if (codeItem == null) {
                    memory.exception = ExceptionValue("InvokeVirtualRange: Method not found")
                    return pc + insnLength
                }
                memory.returnValue = executeMethod(codeItem, environment, args, count, depth + 1)
                return pc + insnLength
            }

            is MethodWrapper.Mocked -> {
                methodName = method.mockedMethod.name
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
    override fun toString(): String {
        val registerIndices = (CCCC until CCCC + count).toList()
        val args = Array(count) { registerIndices[it] }
        return "InvokeSuperRange($methodName) ($count args: ${args.joinToString(",")})"
    }
}

class InvokeDirectRange(pc: Int, code: CodeItem) : InvokeVirtualRange(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val registerIndices = (CCCC until CCCC + count).toList()
        val args = Array(count) { memory.registers[registerIndices[it]] }
        val instance = args.firstOrNull()
        if (instance !is RegisterValue.ObjectRef) {
            memory.exception = ExceptionValue("InvokeVirtual: Not an object reference")
            return pc + insnLength
        }
        val method = environment.getMethod(code, BBBB, instance, direct = true, false)
        when (method) {
            is MethodWrapper.Encoded -> {
                val codeItem = method.encodedMethod.codeItem
                methodName = method.encodedMethod.methodId.name
                if (codeItem == null) {
                    memory.exception = ExceptionValue("InvokeVirtualRange: Method not found")
                    return pc + insnLength
                }
                memory.returnValue = executeMethod(codeItem, environment, args, count, depth + 1)
                return pc + insnLength
            }

            is MethodWrapper.Mocked -> {
                methodName = method.mockedMethod.name
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
    // TODO
    override fun toString(): String {
        val registerIndices = (CCCC until CCCC + count).toList()
        val args = Array(count) { registerIndices[it] }
        return "InvokeDirectRange($methodName) ($count args: ${args.joinToString(",")})"
    }
}

class InvokeStaticRange(pc: Int, val code: CodeItem) : Instruction._3rc(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val method = environment.getMethod(code, BBBB, null, direct = true, false)
        val registerIndices = (CCCC until CCCC + count).toList()
        val args = Array(count) { memory.registers[registerIndices[it]] }
        when (method) {
            is MethodWrapper.Encoded -> {
                val codeItem = method.encodedMethod.codeItem
                methodName = method.encodedMethod.methodId.name
                if (codeItem == null) {
                    memory.exception = ExceptionValue("InvokeVirtualRange: Method not found")
                    return pc + insnLength
                }
                memory.returnValue = executeMethod(codeItem, environment, args, count, depth + 1)
                return pc + insnLength
            }

            is MethodWrapper.Mocked -> {
                methodName = method.mockedMethod.name
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

    private var methodName: String = ""
    override fun toString(): String {
        val registerIndices = (CCCC until CCCC + count).toList()
        val args = Array(count) { registerIndices[it] }
        return "InvokeStaticRange($methodName) ($count args: ${args.joinToString(",")}"
    }
}

class InvokeInterfaceRange(pc: Int, code: CodeItem) : InvokeVirtualRange(pc, code) {
    override fun toString(): String {
        val registerIndices = (CCCC until CCCC + count).toList()
        val args = Array(count) { registerIndices[it] }
        return "InvokeInterfaceRange($methodName) ($count args: ${args.joinToString(",")})"
    }
}

class InvokePolymorphic(pc: Int, code: CodeItem) : Instruction._45cc(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        return pc + insnLength
    }

    override fun toString(): String {
        return "InvokePolymorphic"
    }
}

class InvokePolymorphicRange(pc: Int, code: CodeItem) : Instruction._4rcc(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        return pc + insnLength
    }

    override fun toString(): String {
        return "InvokePolymorphicRange"
    }
}

class InvokeCustom(pc: Int, code: CodeItem) : Instruction._35c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        return pc + insnLength
    }

    override fun toString(): String {
        return "InvokeCustom"
    }
}

class InvokeCustomRange(pc: Int, code: CodeItem) : Instruction._3rc(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        return pc + insnLength
    }

    override fun toString(): String {
        return "InvokeCustomRange"
    }
}

class ConstMethodHandle(pc: Int, code: CodeItem) : Instruction._3rc(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        return pc + insnLength
    }

    override fun toString(): String {
        return "ConstMethodHandle"
    }
}

class ConstMethodType(pc: Int, code: CodeItem) : Instruction._3rc(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        return pc + insnLength
    }

    override fun toString(): String {
        return "ConstMethodType"
    }
}



