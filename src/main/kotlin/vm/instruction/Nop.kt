package com.yhs0602.vm.instruction

import com.yhs0602.vm.Frame
import com.yhs0602.vm.Memory

object Nop : Instruction._10x(0x00) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        return pc + 1
    }
}