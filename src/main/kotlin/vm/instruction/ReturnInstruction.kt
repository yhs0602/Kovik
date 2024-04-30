package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Frame

class ReturnVoid(pc: Int, code: CodeItem) : Instruction._10x(0x0e) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

class Return(pc: Int, code: CodeItem) : Instruction._11x(pc) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

class ReturnWide(pc: Int, code: CodeItem) : Instruction._11x(pc) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

class ReturnObject(pc: Int, code: CodeItem) : Instruction._11x(pc) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}