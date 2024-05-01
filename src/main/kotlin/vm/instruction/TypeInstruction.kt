package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.ExceptionValue
import com.yhs0602.vm.Memory

class CheckCast(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        // TODO: Check if B is a primitive type
        if (this.KindBBBB) {
            memory.exception = ExceptionValue("CheckCast: B is a primitive type")
        }
        return pc + insnLength
    }
}

class InstanceOf(pc: Int, code: CodeItem) : Instruction._22c(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + insnLength
    }
}

