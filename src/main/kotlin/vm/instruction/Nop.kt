package com.yhs0602.vm.instruction

import com.yhs0602.vm.Environment
import com.yhs0602.vm.Memory

object Nop : Instruction._10x(0x00) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }

    override fun toString(): String {
        return "Nop"
    }
}