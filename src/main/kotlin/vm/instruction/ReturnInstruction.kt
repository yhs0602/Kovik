package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Environment
import com.yhs0602.vm.Memory

class ReturnVoid(pc: Int, code: CodeItem) : Instruction._10x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        return -1
    }

    override fun toString(): String {
        return "ReturnVoid"
    }
}

class Return(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        memory.returnValue = arrayOf(memory.registers[vAA])
        return -1
    }

    override fun toString(): String {
        return "Return v$vAA"
    }
}

class ReturnWide(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        memory.returnValue = arrayOf(memory.registers[vAA], memory.registers[vAA + 1])
        return -1
    }

    override fun toString(): String {
        return "ReturnWide v$vAA"
    }
}

class ReturnObject(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        memory.returnValue = arrayOf(memory.registers[vAA])
        return -1
    }

    override fun toString(): String {
        return "ReturnObject v$vAA"
    }
}