package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Environment
import com.yhs0602.vm.ExceptionValue
import com.yhs0602.vm.Memory
import com.yhs0602.vm.RegisterValue


open class CmpFloat(pc: Int, code: CodeItem, val bias: Int) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vBB]
        val value2 = memory.registers[vCC]
        if (value1 !is RegisterValue.Int || value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("CmplFloat: Not a float")
            return pc + insnLength
        }
        val value1Float = java.lang.Float.intBitsToFloat(value1.value)
        val value2Float = java.lang.Float.intBitsToFloat(value2.value)
        // Check NaN
        if (value1Float.isNaN() || value2Float.isNaN()) {
            memory.registers[vAA] = RegisterValue.Int(bias)
            return pc + insnLength
        }
        val result = when {
            value1Float < value2Float -> -1
            value1Float > value2Float -> 1
            else -> 0
        }
        memory.registers[vAA] = RegisterValue.Int(result)
        return pc + insnLength
    }
}

class CmplFloat(pc: Int, code: CodeItem) : CmpFloat(pc, code, -1)

class CmpgFloat(pc: Int, code: CodeItem) : CmpFloat(pc, code, 1)

open class CmpDouble(pc: Int, code: CodeItem, val bias: Int) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vBB]
        val value11 = memory.registers[vBB + 1]
        val value2 = memory.registers[vCC]
        val value22 = memory.registers[vCC + 1]
        if (value1 !is RegisterValue.Int || value2 !is RegisterValue.Int || value11 !is RegisterValue.Int || value22 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("CmplDouble: Not a double")
            return pc + insnLength
        }
        val value1Double = java.lang.Double.longBitsToDouble(value1.value.toLong() shl 32 or value11.value.toLong())
        val value2Double = java.lang.Double.longBitsToDouble(value2.value.toLong() shl 32 or value22.value.toLong())

        // Check NaN
        if (value1Double.isNaN() || value2Double.isNaN()) {
            memory.registers[vAA] = RegisterValue.Int(bias)
            return pc + insnLength
        }
        val result = when {
            value1Double < value2Double -> -1
            value1Double > value2Double -> 1
            else -> 0
        }
        memory.registers[vAA] = RegisterValue.Int(result)
        return pc + insnLength
    }
}


class CmplDouble(pc: Int, code: CodeItem) : CmpDouble(pc, code, -1)

class CmpgDouble(pc: Int, code: CodeItem) : CmpDouble(pc, code, 1)

class CmpLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vBB]
        val value11 = memory.registers[vBB + 1]
        val value2 = memory.registers[vCC]
        val value22 = memory.registers[vCC + 1]
        if (value1 !is RegisterValue.Int || value2 !is RegisterValue.Int || value11 !is RegisterValue.Int || value22 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("CmpLong: Not a long")
            return pc + insnLength
        }
        val value1Long = value1.value.toLong() shl 32 or value11.value.toLong()
        val value2Long = value2.value.toLong() shl 32 or value22.value.toLong()
        val result = when {
            value1Long < value2Long -> -1
            value1Long > value2Long -> 1
            else -> 0
        }
        memory.registers[vAA] = RegisterValue.Int(result)
        return pc + insnLength
    }
}