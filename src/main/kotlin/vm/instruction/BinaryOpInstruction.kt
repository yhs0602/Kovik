package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Environment
import com.yhs0602.vm.ExceptionValue
import com.yhs0602.vm.Memory
import com.yhs0602.vm.RegisterValue

class AddInt(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value2 = memory.registers[vCC]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Int")
            return pc + insnLength
        }
        memory.registers[vAA] = RegisterValue.Int(value1.value + value2.value)
        return pc + insnLength
    }

    override fun toString(): String {
        return "AddInt v$vAA <- v$vBB, v$vCC"
    }
}

class SubInt(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value2 = memory.registers[vCC]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Int")
            return pc + insnLength
        }
        memory.registers[vAA] = RegisterValue.Int(value1.value - value2.value)
        return pc + insnLength
    }
}

class MulInt(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value2 = memory.registers[vCC]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Int")
            return pc + insnLength
        }
        memory.registers[vAA] = RegisterValue.Int(value1.value * value2.value)
        return pc + insnLength
    }
}

class DivInt(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value2 = memory.registers[vCC]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Int")
            return pc + insnLength
        }
        if (value2.value == 0) {
            memory.exception = ExceptionValue("Divide by zero")
            return pc + insnLength
        }
        memory.registers[vAA] = RegisterValue.Int(value1.value / value2.value)
        return pc + insnLength
    }
}

class RemInt(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value2 = memory.registers[vCC]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Int")
            return pc + insnLength
        }
        if (value2.value == 0) {
            memory.exception = ExceptionValue("Divide by zero")
            return pc + insnLength
        }
        memory.registers[vAA] = RegisterValue.Int(value1.value % value2.value)
        return pc + insnLength
    }
}

class AndInt(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value2 = memory.registers[vCC]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Int")
            return pc + insnLength
        }
        memory.registers[vAA] = RegisterValue.Int(value1.value and value2.value)
        return pc + insnLength
    }
}

class OrInt(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value2 = memory.registers[vCC]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Int")
            return pc + insnLength
        }
        memory.registers[vAA] = RegisterValue.Int(value1.value or value2.value)
        return pc + insnLength
    }
}

class XorInt(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value2 = memory.registers[vCC]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Int")
            return pc + insnLength
        }
        memory.registers[vAA] = RegisterValue.Int(value1.value xor value2.value)
        return pc + insnLength
    }
}

class ShlInt(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value2 = memory.registers[vCC]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Int")
            return pc + insnLength
        }
        memory.registers[vAA] = RegisterValue.Int(value1.value shl value2.value)
        return pc + insnLength
    }
}

class ShrInt(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value2 = memory.registers[vCC]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Int")
            return pc + insnLength
        }
        memory.registers[vAA] = RegisterValue.Int(value1.value shr value2.value)
        return pc + insnLength
    }
}

class UshrInt(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value2 = memory.registers[vCC]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Int")
            return pc + insnLength
        }
        memory.registers[vAA] = RegisterValue.Int(value1.value ushr value2.value)
        return pc + insnLength
    }
}

class AddLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value11 = memory.registers[vBB + 1]
        val value2 = memory.registers[vCC]
        val value22 = memory.registers[vCC + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value22 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Int")
            return pc + insnLength
        }
        val value1Long = value1.value.toLong() shl 32 or value11.value.toLong()
        val value2Long = value2.value.toLong() shl 32 or value22.value.toLong()
        val result = value1Long + value2Long
        memory.registers[vAA] = RegisterValue.Int((result shr 32).toInt())
        memory.registers[vAA + 1] = RegisterValue.Int(result.toInt())
        return pc + insnLength
    }

    override fun toString(): String {
        return "AddLong v$vAA <- v$vBB + v$vCC"
    }
}

class SubLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value11 = memory.registers[vBB + 1]
        val value2 = memory.registers[vCC]
        val value22 = memory.registers[vCC + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value22 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Int")
            return pc + insnLength
        }
        val value1Long = value1.value.toLong() shl 32 or value11.value.toLong()
        val value2Long = value2.value.toLong() shl 32 or value22.value.toLong()
        val result = value1Long - value2Long
        memory.registers[vAA] = RegisterValue.Int((result shr 32).toInt())
        memory.registers[vAA + 1] = RegisterValue.Int(result.toInt())
        return pc + insnLength
    }
}

class MulLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value11 = memory.registers[vBB + 1]
        val value2 = memory.registers[vCC]
        val value22 = memory.registers[vCC + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value22 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Int")
            return pc + insnLength
        }
        val value1Long = value1.value.toLong() shl 32 or value11.value.toLong()
        val value2Long = value2.value.toLong() shl 32 or value22.value.toLong()
        val result = value1Long * value2Long
        memory.registers[vAA] = RegisterValue.Int((result shr 32).toInt())
        memory.registers[vAA + 1] = RegisterValue.Int(result.toInt())
        return pc + insnLength
    }
}

class DivLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value11 = memory.registers[vBB + 1]
        val value2 = memory.registers[vCC]
        val value22 = memory.registers[vCC + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value22 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Int")
            return pc + insnLength
        }
        val value1Long = value1.value.toLong() shl 32 or value11.value.toLong()
        val value2Long = value2.value.toLong() shl 32 or value22.value.toLong()
        if (value2Long == 0L) {
            memory.exception = ExceptionValue("Divide by zero")
            return pc + insnLength
        }
        val result = value1Long / value2Long
        memory.registers[vAA] = RegisterValue.Int((result shr 32).toInt())
        memory.registers[vAA + 1] = RegisterValue.Int(result.toInt())
        return pc + insnLength
    }
}

class RemLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value11 = memory.registers[vBB + 1]
        val value2 = memory.registers[vCC]
        val value22 = memory.registers[vCC + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value22 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Int")
            return pc + insnLength
        }
        val value1Long = value1.value.toLong() shl 32 or value11.value.toLong()
        val value2Long = value2.value.toLong() shl 32 or value22.value.toLong()
        if (value2Long == 0L) {
            memory.exception = ExceptionValue("Divide by zero")
            return pc + insnLength
        }
        val result = value1Long % value2Long
        memory.registers[vAA] = RegisterValue.Int((result shr 32).toInt())
        memory.registers[vAA + 1] = RegisterValue.Int(result.toInt())
        return pc + insnLength
    }
}

class AndLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value11 = memory.registers[vBB + 1]
        val value2 = memory.registers[vCC]
        val value22 = memory.registers[vCC + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value22 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Int")
            return pc + insnLength
        }
        val value1Long = value1.value.toLong() shl 32 or value11.value.toLong()
        val value2Long = value2.value.toLong() shl 32 or value22.value.toLong()
        val result = value1Long and value2Long
        memory.registers[vAA] = RegisterValue.Int((result shr 32).toInt())
        memory.registers[vAA + 1] = RegisterValue.Int(result.toInt())
        return pc + insnLength
    }
}

class OrLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value11 = memory.registers[vBB + 1]
        val value2 = memory.registers[vCC]
        val value22 = memory.registers[vCC + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value22 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Int")
            return pc + insnLength
        }
        val value1Long = value1.value.toLong() shl 32 or value11.value.toLong()
        val value2Long = value2.value.toLong() shl 32 or value22.value.toLong()
        val result = value1Long or value2Long
        memory.registers[vAA] = RegisterValue.Int((result shr 32).toInt())
        memory.registers[vAA + 1] = RegisterValue.Int(result.toInt())
        return pc + insnLength
    }
}

class XorLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value11 = memory.registers[vBB + 1]
        val value2 = memory.registers[vCC]
        val value22 = memory.registers[vCC + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value22 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Int")
            return pc + insnLength
        }
        val value1Long = value1.value.toLong() shl 32 or value11.value.toLong()
        val value2Long = value2.value.toLong() shl 32 or value22.value.toLong()
        val result = value1Long xor value2Long
        memory.registers[vAA] = RegisterValue.Int((result shr 32).toInt())
        memory.registers[vAA + 1] = RegisterValue.Int(result.toInt())
        return pc + insnLength
    }
}

class ShlLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value11 = memory.registers[vBB + 1]
        val value2 = memory.registers[vCC]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Int")
            return pc + insnLength
        }
        val value1Long = value1.value.toLong() shl 32 or value11.value.toLong()
        val result = value1Long shl value2.value
        memory.registers[vAA] = RegisterValue.Int((result shr 32).toInt())
        memory.registers[vAA + 1] = RegisterValue.Int(result.toInt())
        return pc + insnLength
    }
}

class ShrLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value11 = memory.registers[vBB + 1]
        val value2 = memory.registers[vCC]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Int")
            return pc + insnLength
        }
        val value1Long = value1.value.toLong() shl 32 or value11.value.toLong()
        val result = value1Long shr value2.value
        memory.registers[vAA] = RegisterValue.Int((result shr 32).toInt())
        memory.registers[vAA + 1] = RegisterValue.Int(result.toInt())
        return pc + insnLength
    }
}

class UshrLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value11 = memory.registers[vBB + 1]
        val value2 = memory.registers[vCC]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Int")
            return pc + insnLength
        }
        val value1Long = value1.value.toLong() shl 32 or value11.value.toLong()
        val result = value1Long ushr value2.value
        memory.registers[vAA] = RegisterValue.Int((result shr 32).toInt())
        memory.registers[vAA + 1] = RegisterValue.Int(result.toInt())
        return pc + insnLength
    }
}

class AddFloat(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value2 = memory.registers[vCC]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Float")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Float")
            return pc + insnLength
        }
        val floatValue1 = java.lang.Float.intBitsToFloat(value1.value)
        val floatValue2 = java.lang.Float.intBitsToFloat(value2.value)
        val result = floatValue1 + floatValue2
        memory.registers[vAA] = RegisterValue.Int(java.lang.Float.floatToRawIntBits(result))
        return pc + insnLength
    }
}

class SubFloat(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value2 = memory.registers[vCC]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Float")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Float")
            return pc + insnLength
        }
        val floatValue1 = java.lang.Float.intBitsToFloat(value1.value)
        val floatValue2 = java.lang.Float.intBitsToFloat(value2.value)
        val result = floatValue1 - floatValue2
        memory.registers[vAA] = RegisterValue.Int(java.lang.Float.floatToRawIntBits(result))
        return pc + insnLength
    }
}

class MulFloat(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value2 = memory.registers[vCC]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Float")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Float")
            return pc + insnLength
        }
        val floatValue1 = java.lang.Float.intBitsToFloat(value1.value)
        val floatValue2 = java.lang.Float.intBitsToFloat(value2.value)
        val result = floatValue1 * floatValue2
        memory.registers[vAA] = RegisterValue.Int(java.lang.Float.floatToRawIntBits(result))
        return pc + insnLength
    }
}

class DivFloat(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value2 = memory.registers[vCC]
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Float")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Float")
            return pc + insnLength
        }
        val floatValue1 = java.lang.Float.intBitsToFloat(value1.value)
        val floatValue2 = java.lang.Float.intBitsToFloat(value2.value)
        val result = floatValue1 / floatValue2
        memory.registers[vAA] = RegisterValue.Int(java.lang.Float.floatToRawIntBits(result))
        return pc + insnLength
    }
}

class RemFloat(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value2 = memory.registers[vCC]

        if (value1 !is RegisterValue.Int || value2 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("Operands must be floats")
            return pc + insnLength
        }

        val floatValue1 = java.lang.Float.intBitsToFloat(value1.value)
        val floatValue2 = java.lang.Float.intBitsToFloat(value2.value)

        if (floatValue2 == 0.0f) {
            memory.exception = ExceptionValue("Division by zero")
            return pc + insnLength
        }

        val result = floatValue1 % floatValue2 // Calculate remainder
        memory.registers[vAA] = RegisterValue.Int(java.lang.Float.floatToRawIntBits(result))

        return pc + insnLength
    }
}

class AddDouble(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value11 = memory.registers[vBB + 1]
        val value2 = memory.registers[vCC]
        val value22 = memory.registers[vCC + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Double")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value22 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Double")
            return pc + insnLength
        }
        val doubleValue1 = java.lang.Double.longBitsToDouble(value1.value.toLong() shl 32 or value11.value.toLong())
        val doubleValue2 = java.lang.Double.longBitsToDouble(value2.value.toLong() shl 32 or value22.value.toLong())
        val result = doubleValue1 + doubleValue2
        val resultBits = java.lang.Double.doubleToRawLongBits(result)
        memory.registers[vAA] = RegisterValue.Int((resultBits shr 32).toInt())
        memory.registers[vAA + 1] = RegisterValue.Int(resultBits.toInt())
        return pc + insnLength
    }
}

class SubDouble(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value11 = memory.registers[vBB + 1]
        val value2 = memory.registers[vCC]
        val value22 = memory.registers[vCC + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Double")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value22 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Double")
            return pc + insnLength
        }
        val doubleValue1 = java.lang.Double.longBitsToDouble(value1.value.toLong() shl 32 or value11.value.toLong())
        val doubleValue2 = java.lang.Double.longBitsToDouble(value2.value.toLong() shl 32 or value22.value.toLong())
        val result = doubleValue1 - doubleValue2
        val resultBits = java.lang.Double.doubleToRawLongBits(result)
        memory.registers[vAA] = RegisterValue.Int((resultBits shr 32).toInt())
        memory.registers[vAA + 1] = RegisterValue.Int(resultBits.toInt())
        return pc + insnLength
    }
}

class MulDouble(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value11 = memory.registers[vBB + 1]
        val value2 = memory.registers[vCC]
        val value22 = memory.registers[vCC + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Double")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value22 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Double")
            return pc + insnLength
        }
        val doubleValue1 = java.lang.Double.longBitsToDouble(value1.value.toLong() shl 32 or value11.value.toLong())
        val doubleValue2 = java.lang.Double.longBitsToDouble(value2.value.toLong() shl 32 or value22.value.toLong())
        val result = doubleValue1 * doubleValue2
        val resultBits = java.lang.Double.doubleToRawLongBits(result)
        memory.registers[vAA] = RegisterValue.Int((resultBits shr 32).toInt())
        memory.registers[vAA + 1] = RegisterValue.Int(resultBits.toInt())
        return pc + insnLength
    }
}

class DivDouble(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value11 = memory.registers[vBB + 1]
        val value2 = memory.registers[vCC]
        val value22 = memory.registers[vCC + 1]
        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Double")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value22 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vCC is not Double")
            return pc + insnLength
        }
        val doubleValue1 = java.lang.Double.longBitsToDouble(value1.value.toLong() shl 32 or value11.value.toLong())
        val doubleValue2 = java.lang.Double.longBitsToDouble(value2.value.toLong() shl 32 or value22.value.toLong())
        if (doubleValue2 == 0.0) {
            memory.exception = ExceptionValue("Divide by zero")
            return pc + insnLength
        }
        val result = doubleValue1 / doubleValue2
        val resultBits = java.lang.Double.doubleToRawLongBits(result)
        memory.registers[vAA] = RegisterValue.Int((resultBits shr 32).toInt())
        memory.registers[vAA + 1] = RegisterValue.Int(resultBits.toInt())
        return pc + insnLength
    }
}

class RemDouble(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        val value1 = memory.registers[vBB]
        val value11 = memory.registers[vBB + 1]
        val value2 = memory.registers[vCC]
        val value22 = memory.registers[vCC + 1]

        if (value1 !is RegisterValue.Int || value11 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("Operands must be doubles")
            return pc + insnLength
        }
        if (value2 !is RegisterValue.Int || value22 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("Operands must be doubles")
            return pc + insnLength
        }

        val doubleValue1 = java.lang.Double.longBitsToDouble(value1.value.toLong() shl 32 or value11.value.toLong())
        val doubleValue2 = java.lang.Double.longBitsToDouble(value2.value.toLong() shl 32 or value22.value.toLong())

        if (doubleValue2 == 0.0) {
            memory.exception = ExceptionValue("Division by zero")
            return pc + insnLength
        }

        val result = doubleValue1 % doubleValue2 // Calculate remainder
        val resultBits = java.lang.Double.doubleToRawLongBits(result)
        memory.registers[vAA] = RegisterValue.Int((resultBits shr 32).toInt())
        memory.registers[vAA + 1] = RegisterValue.Int(resultBits.toInt())

        return pc + insnLength
    }
}