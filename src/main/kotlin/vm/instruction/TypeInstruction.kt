package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Frame

class CheckCast(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

class InstanceOf(pc: Int, code: CodeItem) : Instruction._22c(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

