package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Environment
import com.yhs0602.vm.Memory

class CmplFloat(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class CmpgFloat(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class CmplDouble(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class CmpgDouble(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class CmpLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}