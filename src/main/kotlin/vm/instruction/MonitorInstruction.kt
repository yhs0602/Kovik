package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Memory

class MonitorEnter(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        // TODO
        return pc + insnLength
    }
}

class MonitorExit(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        // TODO
        return pc + insnLength
    }
}