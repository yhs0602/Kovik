package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Memory

class InvokeVirtual(pc: Int, code: CodeItem) : Instruction._35c(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class InvokeSuper(pc: Int, code: CodeItem) : Instruction._35c(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class InvokeDirect(pc: Int, code: CodeItem) : Instruction._35c(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class InvokeStatic(pc: Int, code: CodeItem) : Instruction._35c(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class InvokeInterface(pc: Int, code: CodeItem) : Instruction._35c(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class InvokeVirtualRange(pc: Int, code: CodeItem) : Instruction._3rc(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class InvokeSuperRange(pc: Int, code: CodeItem) : Instruction._3rc(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class InvokeDirectRange(pc: Int, code: CodeItem) : Instruction._3rc(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class InvokeStaticRange(pc: Int, code: CodeItem) : Instruction._3rc(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class InvokeInterfaceRange(pc: Int, code: CodeItem) : Instruction._3rc(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class InvokePolymorphic(pc: Int, code: CodeItem) : Instruction._45cc(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class InvokePolymorphicRange(pc: Int, code: CodeItem) : Instruction._4rcc(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class InvokeCustom(pc: Int, code: CodeItem) : Instruction._35c(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class InvokeCustomRange(pc: Int, code: CodeItem) : Instruction._3rc(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class ConstMethodHandle(pc: Int, code: CodeItem) : Instruction._3rc(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class ConstMethodType(pc: Int, code: CodeItem) : Instruction._3rc(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}



