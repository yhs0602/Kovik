@file:OptIn(ExperimentalUnsignedTypes::class)

package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Environment
import com.yhs0602.vm.ExceptionValue
import com.yhs0602.vm.Memory
import com.yhs0602.vm.RegisterValue

class PackedSwitch(pc: Int, val code: CodeItem) : Instruction._31t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
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
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
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
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val regValue1 = memory.registers[vA]
        val regValue2 = memory.registers[vB]
        if (regValue1 == regValue2) {
            return pc + offset
        }
        return pc + insnLength
    }

    override fun toString(): String {
        return "IfEq v$vA == v$vB, $offset"
    }
}

class IfNe(pc: Int, code: CodeItem) : Instruction._22t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val regValue1 = memory.registers[vA]
        val regValue2 = memory.registers[vB]
        if (regValue1 != regValue2) {
            return pc + offset
        }
        return pc + insnLength
    }
}

class IfLt(pc: Int, code: CodeItem) : Instruction._22t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val regValue1 = memory.registers[vA]
        val regValue2 = memory.registers[vB]
        if (regValue1 !is RegisterValue.Int || regValue2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("IfLt: Not an integer")
            return pc + insnLength
        }
        if (regValue1.value < regValue2.value) {
            return pc + offset
        }
        return pc + insnLength
    }
}

class IfGe(pc: Int, code: CodeItem) : Instruction._22t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val regValue1 = memory.registers[vA]
        val regValue2 = memory.registers[vB]
        if (regValue1 !is RegisterValue.Int || regValue2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("IfGe: Not an integer")
            return pc + insnLength
        }
        if (regValue1.value >= regValue2.value) {
            return pc + offset
        }
        return pc + insnLength
    }

    override fun toString(): String {
        return "IfGe v$vA >= v$vB -> +$offset"
    }
}

class IfGt(pc: Int, code: CodeItem) : Instruction._22t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val regValue1 = memory.registers[vA]
        val regValue2 = memory.registers[vB]
        if (regValue1 !is RegisterValue.Int || regValue2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("IfGt: Not an integer")
            return pc + insnLength
        }
        if (regValue1.value > regValue2.value) {
            return pc + offset
        }
        return pc + insnLength
    }

    override fun toString(): String {
        return "IfGt v$vA > v$vB -> +$offset"
    }
}

class IfLe(pc: Int, code: CodeItem) : Instruction._22t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val regValue1 = memory.registers[vA]
        val regValue2 = memory.registers[vB]
        if (regValue1 !is RegisterValue.Int || regValue2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("IfLe: Not an integer")
            return pc + insnLength
        }
        if (regValue1.value <= regValue2.value) {
            return pc + offset
        }
        return pc + insnLength
    }

    override fun toString(): String {
        return "IfLe v$vA <= v$vB -> +$offset"
    }
}

class IfEqz(pc: Int, code: CodeItem) : Instruction._21t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        when (val regValue = memory.registers[vAA]) {
            is RegisterValue.Int -> {
                return if (regValue.value == 0) {
                    pc + offset
                } else {
                    pc + insnLength
                }
            }

            is RegisterValue.ObjectRef -> {
                return if (regValue.value == null) {
                    pc + offset
                } else {
                    pc + insnLength
                }
            }

            else -> {
                memory.exception = ExceptionValue("IfEqz: Not an integer or object reference")
                return pc + insnLength
            }
        }
    }

    override fun toString(): String {
        return "IfEqz v$vAA == 0 -> +$offset"
    }
}

class IfNez(pc: Int, code: CodeItem) : Instruction._21t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        when (val regValue = memory.registers[vAA]) {
            is RegisterValue.Int -> {
                return if (regValue.value != 0) {
                    pc + offset
                } else {
                    pc + insnLength
                }
            }

            is RegisterValue.ObjectRef -> {
                return if (regValue.value != null) {
                    pc + offset
                } else {
                    pc + insnLength
                }
            }

            else -> {
                memory.exception = ExceptionValue("IfNez: Not an integer or object reference")
                return pc + insnLength
            }
        }
    }
}

class IfLtz(pc: Int, code: CodeItem) : Instruction._21t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val regValue = memory.registers[vAA]
        if (regValue !is RegisterValue.Int) {
            memory.exception = ExceptionValue("IfLtz: Not an integer")
            return pc + insnLength
        }
        return if (regValue.value < 0) {
            pc + offset
        } else {
            pc + insnLength
        }
    }
}

class IfGez(pc: Int, code: CodeItem) : Instruction._21t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val regValue = memory.registers[vAA]
        if (regValue !is RegisterValue.Int) {
            memory.exception = ExceptionValue("IfGez: Not an integer")
            return pc + insnLength
        }
        return if (regValue.value >= 0) {
            pc + offset
        } else {
            pc + insnLength
        }
    }
}

class IfGtz(pc: Int, code: CodeItem) : Instruction._21t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val regValue = memory.registers[vAA]
        if (regValue !is RegisterValue.Int) {
            memory.exception = ExceptionValue("IfGtz: Not an integer")
            return pc + insnLength
        }
        return if (regValue.value > 0) {
            pc + offset
        } else {
            pc + insnLength
        }
    }
}

class IfLez(pc: Int, code: CodeItem) : Instruction._21t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val regValue = memory.registers[vAA]
        if (regValue !is RegisterValue.Int) {
            memory.exception = ExceptionValue("IfLez: Not an integer")
            return pc + insnLength
        }
        return if (regValue.value <= 0) {
            pc + offset
        } else {
            pc + insnLength
        }
    }
}
