package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Frame

class CmplFloat(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

class CmpgFloat(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

class CmplDouble(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

class CmpgDouble(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

class CmpLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}