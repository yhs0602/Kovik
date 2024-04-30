package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Frame

class MonitorEnter(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

class MonitorExit(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}