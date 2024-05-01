package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Frame
import com.yhs0602.vm.Memory

class ReturnVoid(pc: Int, code: CodeItem) : Instruction._10x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        return memory.returnAddress
    }
}

class Return(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        memory.returnValue = listOf(frame.registers[vAA])
        return memory.returnAddress
    }
}

class ReturnWide(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        memory.returnValue = listOf(frame.registers[vAA], frame.registers[vAA + 1])
        return memory.returnAddress
    }
}

class ReturnObject(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        memory.returnValue = listOf(frame.registers[vAA])
        return memory.returnAddress
    }
}