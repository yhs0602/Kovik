package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Environment
import com.yhs0602.vm.ExceptionValue
import com.yhs0602.vm.Memory
import com.yhs0602.vm.RegisterValue

class CheckCast(pc: Int, val code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val objectRef = memory.registers[vAA]
        if (objectRef !is RegisterValue.ObjectRef) {
            memory.exception = ExceptionValue("CheckCast: Not an object reference")
            return pc + insnLength
        }
        val typeId = environment.getTypeId(code, this.KindBBBB)
        val targetTypeDescriptor = typeId.descriptor
        if (!environment.isInstanceOf(objectRef, targetTypeDescriptor)) {
            memory.exception = ExceptionValue("ClassCastException")
            return pc + insnLength
        }
        return pc + insnLength
    }
}

class InstanceOf(pc: Int, val code: CodeItem) : Instruction._22c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val objectRef = memory.registers[vB]
        if (objectRef !is RegisterValue.ObjectRef) {
            memory.registers[vA] = RegisterValue.Int(0)
            return pc + insnLength
        }
        val typeId = environment.getTypeId(code, KindCCCC)
        val targetTypeDescriptor = typeId.descriptor
        memory.registers[vA] = RegisterValue.Int(
            if (environment.isInstanceOf(objectRef, targetTypeDescriptor)) 1 else 0
        )
        return pc + insnLength
    }
}

