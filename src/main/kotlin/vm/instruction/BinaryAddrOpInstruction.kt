package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Environment
import com.yhs0602.vm.ExceptionValue
import com.yhs0602.vm.Memory
import com.yhs0602.vm.RegisterValue

class AddInt2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value2 = memory.registers[vB]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Int")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value1.value + value2.value)
        return pc + insnLength
    }
}

class SubInt2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value2 = memory.registers[vB]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Int")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value1.value - value2.value)
        return pc + insnLength
    }
}

class MulInt2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value2 = memory.registers[vB]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Int")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value1.value * value2.value)
        return pc + insnLength
    }
}

class DivInt2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value2 = memory.registers[vB]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Int")
            return pc + insnLength
        }
        if (value2.value == 0) {
            memory.exception = ExceptionValue("Divide by zero")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value1.value / value2.value)
        return pc + insnLength
    }
}

class RemInt2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value2 = memory.registers[vB]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Int")
            return pc + insnLength
        }
        if (value2.value == 0) {
            memory.exception = ExceptionValue("Divide by zero")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value1.value % value2.value)
        return pc + insnLength
    }
}

class AndInt2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value2 = memory.registers[vB]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Int")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value1.value and value2.value)
        return pc + insnLength
    }
}

class OrInt2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value2 = memory.registers[vB]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Int")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value1.value or value2.value)
        return pc + insnLength
    }
}

class XorInt2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value2 = memory.registers[vB]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Int")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value1.value xor value2.value)
        return pc + insnLength
    }
}

class ShlInt2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value2 = memory.registers[vB]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Int")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value1.value shl value2.value)
        return pc + insnLength
    }
}

class ShrInt2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value2 = memory.registers[vB]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Int")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value1.value shr value2.value)
        return pc + insnLength
    }
}

class UshrInt2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value2 = memory.registers[vB]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Int")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value1.value ushr value2.value)
        return pc + insnLength
    }
}

class AddLong2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value11 = memory.registers[vA + 1]
        val value2 = memory.registers[vB]
        val value21 = memory.registers[vB + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Long")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value21 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Long")
            return pc + insnLength
        }
        val value1Long = (value11.value.toLong() shl 32) or value1.value.toLong()
        val value2Long = (value21.value.toLong() shl 32) or value2.value.toLong()
        val result = value1Long + value2Long
        memory.registers[vA] = RegisterValue.Int(result.toInt())
        memory.registers[vA + 1] = RegisterValue.Int((result shr 32).toInt())
        return pc + insnLength
    }
}

class SubLong2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value11 = memory.registers[vA + 1]
        val value2 = memory.registers[vB]
        val value21 = memory.registers[vB + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Long")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value21 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Long")
            return pc + insnLength
        }
        val value1Long = (value11.value.toLong() shl 32) or value1.value.toLong()
        val value2Long = (value21.value.toLong() shl 32) or value2.value.toLong()
        val result = value1Long - value2Long
        memory.registers[vA] = RegisterValue.Int(result.toInt())
        memory.registers[vA + 1] = RegisterValue.Int((result shr 32).toInt())
        return pc + insnLength
    }
}

class MulLong2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value11 = memory.registers[vA + 1]
        val value2 = memory.registers[vB]
        val value21 = memory.registers[vB + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Long")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value21 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Long")
            return pc + insnLength
        }
        val value1Long = (value11.value.toLong() shl 32) or value1.value.toLong()
        val value2Long = (value21.value.toLong() shl 32) or value2.value.toLong()
        val result = value1Long * value2Long
        memory.registers[vA] = RegisterValue.Int(result.toInt())
        memory.registers[vA + 1] = RegisterValue.Int((result shr 32).toInt())
        return pc + insnLength
    }
}

class DivLong2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value11 = memory.registers[vA + 1]
        val value2 = memory.registers[vB]
        val value21 = memory.registers[vB + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Long")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value21 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Long")
            return pc + insnLength
        }
        val value1Long = (value11.value.toLong() shl 32) or value1.value.toLong()
        val value2Long = (value21.value.toLong() shl 32) or value2.value.toLong()
        if (value2Long == 0L) {
            memory.exception = ExceptionValue("Divide by zero")
            return pc + insnLength
        }
        val result = value1Long / value2Long
        memory.registers[vA] = RegisterValue.Int(result.toInt())
        memory.registers[vA + 1] = RegisterValue.Int((result shr 32).toInt())
        return pc + insnLength
    }
}

class RemLong2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value11 = memory.registers[vA + 1]
        val value2 = memory.registers[vB]
        val value21 = memory.registers[vB + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Long")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value21 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Long")
            return pc + insnLength
        }
        val value1Long = (value11.value.toLong() shl 32) or value1.value.toLong()
        val value2Long = (value21.value.toLong() shl 32) or value2.value.toLong()
        if (value2Long == 0L) {
            memory.exception = ExceptionValue("Divide by zero")
            return pc + insnLength
        }
        val result = value1Long % value2Long
        memory.registers[vA] = RegisterValue.Int(result.toInt())
        memory.registers[vA + 1] = RegisterValue.Int((result shr 32).toInt())
        return pc + insnLength
    }
}

class AndLong2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value11 = memory.registers[vA + 1]
        val value2 = memory.registers[vB]
        val value21 = memory.registers[vB + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Long")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value21 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Long")
            return pc + insnLength
        }
        val value1Long = (value11.value.toLong() shl 32) or value1.value.toLong()
        val value2Long = (value21.value.toLong() shl 32) or value2.value.toLong()
        val result = value1Long and value2Long
        memory.registers[vA] = RegisterValue.Int(result.toInt())
        memory.registers[vA + 1] = RegisterValue.Int((result shr 32).toInt())
        return pc + insnLength
    }
}

class OrLong2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value11 = memory.registers[vA + 1]
        val value2 = memory.registers[vB]
        val value21 = memory.registers[vB + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Long")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value21 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Long")
            return pc + insnLength
        }
        val value1Long = (value11.value.toLong() shl 32) or value1.value.toLong()
        val value2Long = (value21.value.toLong() shl 32) or value2.value.toLong()
        val result = value1Long or value2Long
        memory.registers[vA] = RegisterValue.Int(result.toInt())
        memory.registers[vA + 1] = RegisterValue.Int((result shr 32).toInt())
        return pc + insnLength
    }
}

class XorLong2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value11 = memory.registers[vA + 1]
        val value2 = memory.registers[vB]
        val value21 = memory.registers[vB + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Long")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value21 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Long")
            return pc + insnLength
        }
        val value1Long = (value11.value.toLong() shl 32) or value1.value.toLong()
        val value2Long = (value21.value.toLong() shl 32) or value2.value.toLong()
        val result = value1Long xor value2Long
        memory.registers[vA] = RegisterValue.Int(result.toInt())
        memory.registers[vA + 1] = RegisterValue.Int((result shr 32).toInt())
        return pc + insnLength
    }
}

class ShlLong2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val shift = memory.registers[vB]
        if (shift !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Int")
            return pc + insnLength
        }
        val value1 = memory.registers[vA]
        val value11 = memory.registers[vA + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Long")
            return pc + insnLength
        }
        val value1Long = (value11.value.toLong() shl 32) or value1.value.toLong()
        val result = value1Long shl shift.value
        memory.registers[vA] = RegisterValue.Int(result.toInt())
        memory.registers[vA + 1] = RegisterValue.Int((result shr 32).toInt())
        return pc + insnLength
    }
}

class ShrLong2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val shift = memory.registers[vB]
        if (shift !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Int")
            return pc + insnLength
        }
        val value1 = memory.registers[vA]
        val value11 = memory.registers[vA + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Long")
            return pc + insnLength
        }
        val value1Long = (value11.value.toLong() shl 32) or value1.value.toLong()
        val result = value1Long shr shift.value
        memory.registers[vA] = RegisterValue.Int(result.toInt())
        memory.registers[vA + 1] = RegisterValue.Int((result shr 32).toInt())
        return pc + insnLength
    }
}

class UshrLong2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val shift = memory.registers[vB]
        if (shift !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Int")
            return pc + insnLength
        }
        val value1 = memory.registers[vA]
        val value11 = memory.registers[vA + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Long")
            return pc + insnLength
        }
        val value1Long = (value11.value.toLong() shl 32) or value1.value.toLong()
        val result = value1Long ushr shift.value
        memory.registers[vA] = RegisterValue.Int(result.toInt())
        memory.registers[vA + 1] = RegisterValue.Int((result shr 32).toInt())
        return pc + insnLength
    }
}

class AddFloat2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value2 = memory.registers[vB]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Float")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Float")
            return pc + insnLength
        }
        val floatValue1 = java.lang.Float.intBitsToFloat(value1.value)
        val floatValue2 = java.lang.Float.intBitsToFloat(value2.value)
        val result = floatValue1 + floatValue2
        memory.registers[vA] = RegisterValue.Int(java.lang.Float.floatToRawIntBits(result))
        return pc + insnLength
    }
}

class SubFloat2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value2 = memory.registers[vB]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Float")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Float")
            return pc + insnLength
        }
        val floatValue1 = java.lang.Float.intBitsToFloat(value1.value)
        val floatValue2 = java.lang.Float.intBitsToFloat(value2.value)
        val result = floatValue1 - floatValue2
        memory.registers[vA] = RegisterValue.Int(java.lang.Float.floatToRawIntBits(result))
        return pc + insnLength
    }
}

class MulFloat2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value2 = memory.registers[vB]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Float")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Float")
            return pc + insnLength
        }
        val floatValue1 = java.lang.Float.intBitsToFloat(value1.value)
        val floatValue2 = java.lang.Float.intBitsToFloat(value2.value)
        val result = floatValue1 * floatValue2
        memory.registers[vA] = RegisterValue.Int(java.lang.Float.floatToRawIntBits(result))
        return pc + insnLength
    }
}

class DivFloat2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value2 = memory.registers[vB]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Float")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Float")
            return pc + insnLength
        }
        val floatValue1 = java.lang.Float.intBitsToFloat(value1.value)
        val floatValue2 = java.lang.Float.intBitsToFloat(value2.value)
        val result = floatValue1 / floatValue2
        memory.registers[vA] = RegisterValue.Int(java.lang.Float.floatToRawIntBits(result))
        return pc + insnLength
    }
}

class RemFloat2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value2 = memory.registers[vB]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Float")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Float")
            return pc + insnLength
        }
        val floatValue1 = java.lang.Float.intBitsToFloat(value1.value)
        val floatValue2 = java.lang.Float.intBitsToFloat(value2.value)
        val result = floatValue1 % floatValue2
        memory.registers[vA] = RegisterValue.Int(java.lang.Float.floatToRawIntBits(result))
        return pc + insnLength
    }
}

class AddDouble2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value11 = memory.registers[vA + 1]
        val value2 = memory.registers[vB]
        val value21 = memory.registers[vB + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Double")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value21 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Double")
            return pc + insnLength
        }
        val value1Double = java.lang.Double.longBitsToDouble((value11.value.toLong() shl 32) or value1.value.toLong())
        val value2Double = java.lang.Double.longBitsToDouble((value21.value.toLong() shl 32) or value2.value.toLong())
        val result = value1Double + value2Double
        val resultBits = java.lang.Double.doubleToRawLongBits(result)
        memory.registers[vA] = RegisterValue.Int(resultBits.toInt())
        memory.registers[vA + 1] = RegisterValue.Int((resultBits shr 32).toInt())
        return pc + insnLength
    }
}

class SubDouble2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value11 = memory.registers[vA + 1]
        val value2 = memory.registers[vB]
        val value21 = memory.registers[vB + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Double")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value21 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Double")
            return pc + insnLength
        }
        val value1Double = java.lang.Double.longBitsToDouble((value11.value.toLong() shl 32) or value1.value.toLong())
        val value2Double = java.lang.Double.longBitsToDouble((value21.value.toLong() shl 32) or value2.value.toLong())
        val result = value1Double - value2Double
        val resultBits = java.lang.Double.doubleToRawLongBits(result)
        memory.registers[vA] = RegisterValue.Int(resultBits.toInt())
        memory.registers[vA + 1] = RegisterValue.Int((resultBits shr 32).toInt())
        return pc + insnLength
    }
}

class MulDouble2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value11 = memory.registers[vA + 1]
        val value2 = memory.registers[vB]
        val value21 = memory.registers[vB + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Double")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value21 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Double")
            return pc + insnLength
        }
        val value1Double = java.lang.Double.longBitsToDouble((value11.value.toLong() shl 32) or value1.value.toLong())
        val value2Double = java.lang.Double.longBitsToDouble((value21.value.toLong() shl 32) or value2.value.toLong())
        val result = value1Double * value2Double
        val resultBits = java.lang.Double.doubleToRawLongBits(result)
        memory.registers[vA] = RegisterValue.Int(resultBits.toInt())
        memory.registers[vA + 1] = RegisterValue.Int((resultBits shr 32).toInt())
        return pc + insnLength
    }
}

class DivDouble2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value11 = memory.registers[vA + 1]
        val value2 = memory.registers[vB]
        val value21 = memory.registers[vB + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Double")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value21 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Double")
            return pc + insnLength
        }
        val value1Double = java.lang.Double.longBitsToDouble((value11.value.toLong() shl 32) or value1.value.toLong())
        val value2Double = java.lang.Double.longBitsToDouble((value21.value.toLong() shl 32) or value2.value.toLong())
        val result = value1Double / value2Double
        val resultBits = java.lang.Double.doubleToRawLongBits(result)
        memory.registers[vA] = RegisterValue.Int(resultBits.toInt())
        memory.registers[vA + 1] = RegisterValue.Int((resultBits shr 32).toInt())
        return pc + insnLength
    }
}

class RemDouble2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vA]
        val value11 = memory.registers[vA + 1]
        val value2 = memory.registers[vB]
        val value21 = memory.registers[vB + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vA is not Double")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value21 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Double")
            return pc + insnLength
        }
        val value1Double = java.lang.Double.longBitsToDouble((value11.value.toLong() shl 32) or value1.value.toLong())
        val value2Double = java.lang.Double.longBitsToDouble((value21.value.toLong() shl 32) or value2.value.toLong())
        val result = value1Double % value2Double
        val resultBits = java.lang.Double.doubleToRawLongBits(result)
        memory.registers[vA] = RegisterValue.Int(resultBits.toInt())
        memory.registers[vA + 1] = RegisterValue.Int((resultBits shr 32).toInt())
        return pc + insnLength
    }
}


