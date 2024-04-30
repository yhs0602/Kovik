package com.yhs0602.vm.instruction

import com.yhs0602.vm.Frame

object Nop : Instruction._10x(0x00) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}