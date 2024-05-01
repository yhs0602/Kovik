package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Environment
import com.yhs0602.vm.ExceptionValue
import com.yhs0602.vm.Memory

class Throw(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val exception = memory.registers[vAA]
        if (exception !is ExceptionValue) {
            memory.exception = ExceptionValue("Throw: Not an object reference")
            return pc + 1
        }
        memory.exception = exception
        return pc + insnLength
    }
}

class Goto(pc: Int, code: CodeItem) : Instruction._10t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + offset
    }
}

class Goto16(pc: Int, code: CodeItem) : Instruction._20t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + offset
    }
}

class Goto32(pc: Int, code: CodeItem) : Instruction._30t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + AAAAhi shl 16 or AAAAlo
    }
}