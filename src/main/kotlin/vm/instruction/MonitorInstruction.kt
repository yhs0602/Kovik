package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Frame
import com.yhs0602.vm.Memory

class MonitorEnter(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        return pc + 1
    }
}

class MonitorExit(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        return pc + 1
    }
}