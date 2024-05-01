package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Frame
import com.yhs0602.vm.Memory

class Throw(pc: Int, code: CodeItem) : Instruction._10x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        return pc + 1
    }
}

class Goto(pc: Int, code: CodeItem) : Instruction._10t(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        return pc + code.getOffset()
    }
}

class Goto16(pc: Int, code: CodeItem) : Instruction._20t(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        return pc + code.getOffset()
    }
}

class Goto32(pc: Int, code: CodeItem) : Instruction._30t(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        return pc + code.getOffset()
    }
}