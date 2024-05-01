package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Frame
import com.yhs0602.vm.Memory

class CmplFloat(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        return pc + 1
    }
}

class CmpgFloat(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        return pc + 1
    }
}

class CmplDouble(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        return pc + 1
    }
}

class CmpgDouble(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        return pc + 1
    }
}

class CmpLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        return pc + 1
    }
}