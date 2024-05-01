package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Frame
import com.yhs0602.vm.Memory

class Const4(pc: Int, code: CodeItem) : Instruction._11n(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        frame.registers[vA] = vB
        return pc + 2
    }
}

class Const16(pc: Int, code: CodeItem) : Instruction._21s(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        frame.registers[vA] = vB
        return pc + 2
    }
}

class Const32(pc: Int, code: CodeItem) : Instruction._31i(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        frame.registers[vA] = vB
        return pc + 2
    }
}

class ConstHigh16(pc: Int, code: CodeItem) : Instruction._51l(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        frame.registers[vA] = vB
        return pc + 2
    }
}

class ConstWide16(pc: Int, code: CodeItem) : Instruction._21s(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        frame.registers[vA] = vB
        return pc + 2
    }
}

class ConstWide32(pc: Int, code: CodeItem) : Instruction._31i(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        frame.registers[vA] = vB
        return pc + 2
    }
}

class ConstWide64(pc: Int, code: CodeItem) : Instruction._51l(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        frame.registers[vA] = vB
        return pc + 2
    }
}

class ConstWideHigh16(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        frame.registers[vA] = vB
        return pc + 2
    }
}

class ConstString(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        frame.registers[vA] = vB
        return pc + 2
    }
}

class ConstStringJumbo(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        frame.registers[vA] = vB
        return pc + 2
    }
}

class ConstClass(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        frame.registers[vA] = vB
        return pc + 2
    }
}