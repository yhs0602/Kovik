@file:OptIn(ExperimentalUnsignedTypes::class)

package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Environment
import com.yhs0602.vm.ExceptionValue
import com.yhs0602.vm.Memory
import com.yhs0602.vm.RegisterValue

class PackedSwitch(pc: Int, val code: CodeItem) : Instruction._31t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val tableDataSignedBranchOffset = BBBBhi shl 16 or BBBBlo
        val ident = code.insns[pc + tableDataSignedBranchOffset].toInt()
        if (ident != 0x0100) {
            memory.exception = ExceptionValue("PackedSwitch: Invalid payload identifier")
            return pc + insnLength
        }
        val size = code.insns[pc + tableDataSignedBranchOffset + 1].toInt()
        val firstKey =
            code.insns[pc + tableDataSignedBranchOffset + 2].toInt() shl 16 or code.insns[pc + tableDataSignedBranchOffset + 3].toInt()
        val targets = IntArray(size) { i ->
            code.insns[pc + tableDataSignedBranchOffset + 4 + i * 2].toInt() shl 16 or code.insns[pc + tableDataSignedBranchOffset + 4 + i * 2 + 1].toInt()
        }
        val regValue = memory.registers[vAA]
        if (regValue !is RegisterValue.Int) {
            memory.exception = ExceptionValue("PackedSwitch: Not an integer")
            return pc + insnLength
        }
        val key = regValue.value
        val target = if (key < firstKey || key >= firstKey + size) {
            pc + insnLength
        } else {
            pc + targets[key - firstKey]
        }
        return target
    }
}

class SparseSwitch(pc: Int, val code: CodeItem) : Instruction._31t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val tableDataSignedBranchOffset = BBBBhi shl 16 or BBBBlo
        val ident = code.insns[pc + tableDataSignedBranchOffset].toInt()
        if (ident != 0x0200) {
            memory.exception = ExceptionValue("SparseSwitch: Invalid payload identifier")
            return pc + insnLength
        }
        val size = code.insns[pc + tableDataSignedBranchOffset + 1].toInt()
        val keys = IntArray(size) { i ->
            code.insns[pc + tableDataSignedBranchOffset + 2 + i * 4].toInt() shl 16 or code.insns[pc + tableDataSignedBranchOffset + 2 + i * 4 + 1].toInt()
        }
        val targets = IntArray(size) { i ->
            code.insns[pc + tableDataSignedBranchOffset + 2 + i * 4 + 2].toInt() shl 16 or code.insns[pc + tableDataSignedBranchOffset + 2 + i * 4 + 3].toInt()
        }
        val regValue = memory.registers[vAA]
        if (regValue !is RegisterValue.Int) {
            memory.exception = ExceptionValue("SparseSwitch: Not an integer")
            return pc + insnLength
        }
        val key = regValue.value
        val target = keys.indexOf(key)
        return if (target == -1) {
            pc + insnLength
        } else {
            pc + targets[target]
        }
    }
}

class IfEq(pc: Int, code: CodeItem) : Instruction._22t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class IfNe(pc: Int, code: CodeItem) : Instruction._22t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class IfLt(pc: Int, code: CodeItem) : Instruction._22t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class IfGe(pc: Int, code: CodeItem) : Instruction._22t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class IfGt(pc: Int, code: CodeItem) : Instruction._22t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class IfLe(pc: Int, code: CodeItem) : Instruction._22t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class IfEqz(pc: Int, code: CodeItem) : Instruction._21t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class IfNez(pc: Int, code: CodeItem) : Instruction._21t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class IfLtz(pc: Int, code: CodeItem) : Instruction._21t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class IfGez(pc: Int, code: CodeItem) : Instruction._21t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class IfGtz(pc: Int, code: CodeItem) : Instruction._21t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class IfLez(pc: Int, code: CodeItem) : Instruction._21t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}
