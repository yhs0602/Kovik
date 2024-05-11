package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Environment
import com.yhs0602.vm.ExceptionValue
import com.yhs0602.vm.Memory
import com.yhs0602.vm.RegisterValue

class CheckCast(pc: Int, val code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val objectRef = memory.registers[vAA]
        if (objectRef !is RegisterValue.ObjectRef) {
            memory.exception = ExceptionValue("CheckCast: Not an object reference")
            return pc + insnLength
        }
        val typeId = environment.getTypeId(code, this.KindBBBB)
        targetTypeDescriptor = typeId.descriptor
        if (!environment.isInstanceOf(objectRef, targetTypeDescriptor, depth)) {
            memory.exception = ExceptionValue("ClassCastException: ${objectRef.typeId.descriptor} cannot be cast to $targetTypeDescriptor")
            return pc + insnLength
        }
        return pc + insnLength
    }

    private var targetTypeDescriptor: String = ""
    override fun toString(): String {
        return "check-cast v$vAA, $KindBBBB($targetTypeDescriptor)"
    }
}

class InstanceOf(pc: Int, val code: CodeItem) : Instruction._22c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val objectRef = memory.registers[vB]
        if (objectRef !is RegisterValue.ObjectRef) {
            memory.registers[vA] = RegisterValue.Int(0)
            return pc + insnLength
        }
        val typeId = environment.getTypeId(code, KindCCCC)
        targetTypeDescriptor = typeId.descriptor
        memory.registers[vA] = RegisterValue.Int(
            if (environment.isInstanceOf(objectRef, targetTypeDescriptor, depth)) 1 else 0
        )
        return pc + insnLength
    }

    private var targetTypeDescriptor: String = ""

    override fun toString(): String {
        return "instance-of v$vA <- v$vB, $KindCCCC ($targetTypeDescriptor)"
    }
}

