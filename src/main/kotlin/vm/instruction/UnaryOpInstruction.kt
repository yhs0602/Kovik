package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Environment
import com.yhs0602.vm.ExceptionValue
import com.yhs0602.vm.Memory
import com.yhs0602.vm.RegisterValue

class NegInt(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value = memory.registers[vB]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("NegInt: Register value is not an integer")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(-value.value)
        return pc + insnLength
    }
}

class NotInt(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value = memory.registers[vB]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("NotInt: Register value is not an integer")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value.value.inv())
        return pc + insnLength
    }
}

class NegLong(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vB]
        val value2 = memory.registers[vB + 1]
        if (value1 !is RegisterValue.Int || value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("NegLong: Register value is not a long")
            return pc + insnLength
        }
        val longValue = (value1.value.toLong() shl 32) or (value2.value.toLong() and 0xFFFFFFFF)
        val negLongValue = -longValue
        memory.registers[vA] = RegisterValue.Int((negLongValue shr 32).toInt())
        memory.registers[vA + 1] = RegisterValue.Int(negLongValue.toInt())
        return pc + insnLength
    }
}

class NotLong(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vB]
        val value2 = memory.registers[vB + 1]
        if (value1 !is RegisterValue.Int || value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("NotLong: Register value is not a long")
            return pc + insnLength
        }
        val longValue = (value1.value.toLong() shl 32) or (value2.value.toLong() and 0xFFFFFFFF)
        val notLongValue = longValue.inv()
        memory.registers[vA] = RegisterValue.Int((notLongValue shr 32).toInt())
        memory.registers[vA + 1] = RegisterValue.Int(notLongValue.toInt())
        return pc + insnLength
    }
}

class NegFloat(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value = memory.registers[vB]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("NegFloat: Register value is not an integer")
            return pc + insnLength
        }
        val floatValue = java.lang.Float.intBitsToFloat(value.value)
        memory.registers[vA] = RegisterValue.Int(java.lang.Float.floatToIntBits(-floatValue))
        return pc + insnLength
    }
}

class NegDouble(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vB]
        val value2 = memory.registers[vB + 1]
        if (value1 !is RegisterValue.Int || value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("NegDouble: Register value is not a long")
            return pc + insnLength
        }
        val doubleValue =
            java.lang.Double.longBitsToDouble((value1.value.toLong() shl 32) or (value2.value.toLong() and 0xFFFFFFFF))
        val negDoubleValue = -doubleValue
        val bits = java.lang.Double.doubleToLongBits(negDoubleValue)
        memory.registers[vA] = RegisterValue.Int((bits shr 32).toInt())
        memory.registers[vA + 1] = RegisterValue.Int(bits.toInt())
        return pc + insnLength
    }
}

class IntToLong(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value = memory.registers[vB]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("IntToLong: Register value is not an integer")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value.value.toLong().toInt())
        memory.registers[vA + 1] = RegisterValue.Int(0)
        return pc + insnLength
    }
}

class IntToFloat(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value = memory.registers[vB]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("IntToFloat: Register value is not an integer")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(java.lang.Float.floatToIntBits(value.value.toFloat()))
        return pc + insnLength
    }
}

class IntToDouble(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value = memory.registers[vB]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("IntToDouble: Register value is not an integer")
            return pc + insnLength
        }
        val doubleValue = value.value.toDouble()
        val bits = java.lang.Double.doubleToLongBits(doubleValue)
        memory.registers[vA] = RegisterValue.Int((bits shr 32).toInt())
        memory.registers[vA + 1] = RegisterValue.Int(bits.toInt())
        return pc + insnLength
    }
}

class LongToInt(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vB]
        val value2 = memory.registers[vB + 1]
        if (value1 !is RegisterValue.Int || value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("LongToInt: Register value is not a long")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value1.value)
        return pc + insnLength
    }
}

class LongToFloat(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vB]
        val value2 = memory.registers[vB + 1]
        if (value1 !is RegisterValue.Int || value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("LongToFloat: Register value is not a long")
            return pc + insnLength
        }
        val longValue = (value1.value.toLong() shl 32) or (value2.value.toLong() and 0xFFFFFFFF)
        memory.registers[vA] = RegisterValue.Int(java.lang.Float.floatToIntBits(longValue.toFloat()))
        return pc + insnLength
    }
}

class LongToDouble(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vB]
        val value2 = memory.registers[vB + 1]
        if (value1 !is RegisterValue.Int || value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("LongToDouble: Register value is not a long")
            return pc + insnLength
        }
        val longValue = (value1.value.toLong() shl 32) or (value2.value.toLong() and 0xFFFFFFFF)
        val doubleValue = longValue.toDouble()
        val bits = java.lang.Double.doubleToLongBits(doubleValue)
        memory.registers[vA] = RegisterValue.Int(bits.toInt())
        memory.registers[vA + 1] = RegisterValue.Int((bits shr 32).toInt())
        return pc + insnLength
    }
}

class FloatToInt(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value = memory.registers[vB]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("FloatToInt: Register value is not an integer")
            return pc + insnLength
        }
        val floatValue = java.lang.Float.intBitsToFloat(value.value)
        memory.registers[vA] = RegisterValue.Int(floatValue.toInt())
        return pc + insnLength
    }
}

class FloatToLong(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value = memory.registers[vB]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("FloatToLong: Register value is not an integer")
            return pc + insnLength
        }
        val floatValue = java.lang.Float.intBitsToFloat(value.value)
        val longValue = floatValue.toLong()
        memory.registers[vA] = RegisterValue.Int(longValue.toInt())
        memory.registers[vA + 1] = RegisterValue.Int((longValue shr 32).toInt())
        return pc + insnLength
    }
}

class FloatToDouble(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value = memory.registers[vB]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("FloatToDouble: Register value is not an integer")
            return pc + insnLength
        }
        val floatValue = java.lang.Float.intBitsToFloat(value.value)
        val doubleValue = floatValue.toDouble()
        val bits = java.lang.Double.doubleToLongBits(doubleValue)
        memory.registers[vA] = RegisterValue.Int(bits.toInt())
        memory.registers[vA + 1] = RegisterValue.Int((bits shr 32).toInt())
        return pc + insnLength
    }
}

class DoubleToInt(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vB]
        val value2 = memory.registers[vB + 1]
        if (value1 !is RegisterValue.Int || value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("DoubleToInt: Register value is not a long")
            return pc + insnLength
        }
        val doubleValue =
            java.lang.Double.longBitsToDouble((value1.value.toLong() shl 32) or (value2.value.toLong() and 0xFFFFFFFF))
        memory.registers[vA] = RegisterValue.Int(doubleValue.toInt())
        return pc + insnLength
    }
}

class DoubleToLong(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vB]
        val value2 = memory.registers[vB + 1]
        if (value1 !is RegisterValue.Int || value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("DoubleToLong: Register value is not a long")
            return pc + insnLength
        }
        val doubleValue =
            java.lang.Double.longBitsToDouble((value1.value.toLong() shl 32) or (value2.value.toLong() and 0xFFFFFFFF))
        val longValue = doubleValue.toLong()
        memory.registers[vA] = RegisterValue.Int(longValue.toInt())
        memory.registers[vA + 1] = RegisterValue.Int((longValue shr 32).toInt())
        return pc + insnLength
    }
}

class DoubleToFloat(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vB]
        val value2 = memory.registers[vB + 1]
        if (value1 !is RegisterValue.Int || value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("DoubleToFloat: Register value is not a long")
            return pc + insnLength
        }
        val doubleValue =
            java.lang.Double.longBitsToDouble((value1.value.toLong() shl 32) or (value2.value.toLong() and 0xFFFFFFFF))
        memory.registers[vA] = RegisterValue.Int(java.lang.Float.floatToIntBits(doubleValue.toFloat()))
        return pc + insnLength
    }
}

class IntToByte(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value = memory.registers[vB]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("IntToByte: Register value is not an integer")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value.value.toByte().toInt())
        return pc + insnLength
    }
}

class IntToChar(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value = memory.registers[vB]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("IntToChar: Register value is not an integer")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value.value.toChar().code)
        return pc + insnLength
    }
}

class IntToShort(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value = memory.registers[vB]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("IntToShort: Register value is not an integer")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value.value.toShort().toInt())
        return pc + insnLength
    }
}

