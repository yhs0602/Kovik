package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Frame
import com.yhs0602.vm.Memory

class CheckCast(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        return pc + 1
    }
}

class InstanceOf(pc: Int, code: CodeItem) : Instruction._22c(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        return pc + 1
    }
}

