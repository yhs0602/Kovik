package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Environment
import com.yhs0602.vm.Memory

class ReturnVoid(pc: Int, code: CodeItem) : Instruction._10x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return -1
    }
}

class Return(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        memory.returnValue = arrayOf(memory.registers[vAA])
        return -1
    }
}

class ReturnWide(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        memory.returnValue = arrayOf(memory.registers[vAA], memory.registers[vAA + 1])
        return -1
    }
}

class ReturnObject(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        memory.returnValue = arrayOf(memory.registers[vAA])
        return -1
    }
}