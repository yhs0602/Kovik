package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Environment
import com.yhs0602.vm.ExceptionValue
import com.yhs0602.vm.Memory
import com.yhs0602.vm.RegisterValue

class AddIntLit16(pc: Int, code: CodeItem) : Instruction._22s(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vB]
        val value2 = this.LCCCC
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Int")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value1.value + value2)
        return pc + insnLength
    }
}

class RsubInt(pc: Int, code: CodeItem) : Instruction._22s(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vB]
        val value2 = this.LCCCC
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Int")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value2 - value1.value)
        return pc + insnLength
    }
}

class MulIntLit16(pc: Int, code: CodeItem) : Instruction._22s(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vB]
        val value2 = this.LCCCC
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Int")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value1.value * value2)
        return pc + insnLength
    }
}

class DivIntLit16(pc: Int, code: CodeItem) : Instruction._22s(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vB]
        val value2 = this.LCCCC
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Int")
            return pc + insnLength
        }
        if (value2 == 0) {
            memory.exception = ExceptionValue("Divide by zero")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value1.value / value2)
        return pc + insnLength
    }
}

class RemIntLit16(pc: Int, code: CodeItem) : Instruction._22s(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vB]
        val value2 = this.LCCCC
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Int")
            return pc + insnLength
        }
        if (value2 == 0) {
            memory.exception = ExceptionValue("Divide by zero")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value1.value % value2)
        return pc + insnLength
    }
}

class AndIntLit16(pc: Int, code: CodeItem) : Instruction._22s(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vB]
        val value2 = this.LCCCC
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Int")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value1.value and value2)
        return pc + insnLength
    }
}

class OrIntLit16(pc: Int, code: CodeItem) : Instruction._22s(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vB]
        val value2 = this.LCCCC
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Int")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value1.value or value2)
        return pc + insnLength
    }
}

class XorIntLit16(pc: Int, code: CodeItem) : Instruction._22s(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vB]
        val value2 = this.LCCCC
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vB is not Int")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(value1.value xor value2)
        return pc + insnLength
    }
}

class AddIntLit8(pc: Int, code: CodeItem) : Instruction._22b(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vBB]
        val value2 = this.LCC
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        memory.registers[vAA] = RegisterValue.Int(value1.value + value2)
        return pc + insnLength
    }

    override fun toString(): String {
        return "AddIntLit8 v$vAA = v$vBB + $LCC"
    }
}

class RsubIntLit8(pc: Int, code: CodeItem) : Instruction._22b(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vBB]
        val value2 = this.LCC
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        memory.registers[vAA] = RegisterValue.Int(value2 - value1.value)
        return pc + insnLength
    }
}

class MulIntLit8(pc: Int, code: CodeItem) : Instruction._22b(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vBB]
        val value2 = this.LCC
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        memory.registers[vAA] = RegisterValue.Int(value1.value * value2)
        return pc + insnLength
    }
}

class DivIntLit8(pc: Int, code: CodeItem) : Instruction._22b(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vBB]
        val value2 = this.LCC
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 == 0) {
            memory.exception = ExceptionValue("Divide by zero")
            return pc + insnLength
        }
        memory.registers[vAA] = RegisterValue.Int(value1.value / value2)
        return pc + insnLength
    }
}

class RemIntLit8(pc: Int, code: CodeItem) : Instruction._22b(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vBB]
        val value2 = this.LCC
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        if (value2 == 0) {
            memory.exception = ExceptionValue("Divide by zero")
            return pc + insnLength
        }
        memory.registers[vAA] = RegisterValue.Int(value1.value % value2)
        return pc + insnLength
    }
}

class AndIntLit8(pc: Int, code: CodeItem) : Instruction._22b(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vBB]
        val value2 = this.LCC
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        memory.registers[vAA] = RegisterValue.Int(value1.value and value2)
        return pc + insnLength
    }
}

class OrIntLit8(pc: Int, code: CodeItem) : Instruction._22b(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vBB]
        val value2 = this.LCC
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        memory.registers[vAA] = RegisterValue.Int(value1.value or value2)
        return pc + insnLength
    }
}

class XorIntLit8(pc: Int, code: CodeItem) : Instruction._22b(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vBB]
        val value2 = this.LCC
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        memory.registers[vAA] = RegisterValue.Int(value1.value xor value2)
        return pc + insnLength
    }
}

class ShlIntLit8(pc: Int, code: CodeItem) : Instruction._22b(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vBB]
        val value2 = this.LCC
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        memory.registers[vAA] = RegisterValue.Int(value1.value shl value2)
        return pc + insnLength
    }
}

class ShrIntLit8(pc: Int, code: CodeItem) : Instruction._22b(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vBB]
        val value2 = this.LCC
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        memory.registers[vAA] = RegisterValue.Int(value1.value shr value2)
        return pc + insnLength
    }
}

class UshrIntLit8(pc: Int, code: CodeItem) : Instruction._22b(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vBB]
        val value2 = this.LCC
        if (value1 !is RegisterValue.Int) {
            memory.exception = ExceptionValue("v$vBB is not Int")
            return pc + insnLength
        }
        memory.registers[vAA] = RegisterValue.Int(value1.value ushr value2)
        return pc + insnLength
    }
}