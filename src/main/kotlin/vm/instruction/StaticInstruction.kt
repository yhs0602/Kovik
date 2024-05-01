package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Environment
import com.yhs0602.vm.ExceptionValue
import com.yhs0602.vm.Memory
import com.yhs0602.vm.RegisterValue

open class Sget(pc: Int, val code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value = environment.getStaticField(code, KindBBBB)
        if (value == null) {
            memory.exception = ExceptionValue("Sget: Field not found")
            return pc + insnLength
        }
        performGet(value, memory)
        return pc + insnLength
    }

    open fun performGet(value: Array<RegisterValue>, memory: Memory) {
        if (value.size != 1) {
            memory.exception = ExceptionValue("Sget: Field size is not 1")
            return
        }
        memory.registers[vAA] = value[0]
    }
}

class SgetWide(pc: Int, code: CodeItem) : Sget(pc, code) {
    override fun performGet(value: Array<RegisterValue>, memory: Memory) {
        if (value.size != 2) {
            memory.exception = ExceptionValue("SgetWide: Field size is not 2")
            return
        }
        memory.registers[vAA] = value[0]
        memory.registers[vAA + 1] = value[1]
    }
}

class SgetObject(pc: Int, code: CodeItem) : Sget(pc, code) {
    override fun performGet(value: Array<RegisterValue>, memory: Memory) {
        if (value.size != 1) {
            memory.exception = ExceptionValue("SgetObject: Field size is not 1")
            return
        }
        if (value[0] !is RegisterValue.ObjectRef) {
            memory.exception = ExceptionValue("SgetObject: Field is not an object reference")
            return
        }
        memory.registers[vAA] = value[0]
    }
}

class SgetBoolean(pc: Int, code: CodeItem) : Sget(pc, code) {
    override fun performGet(value: Array<RegisterValue>, memory: Memory) {
        if (value.size != 1) {
            memory.exception = ExceptionValue("SgetBoolean: Field size is not 1")
            return
        }
        if (value[0] !is RegisterValue.Int) {
            memory.exception = ExceptionValue("SgetBoolean: Field is not an integer")
            return
        }
        memory.registers[vAA] = value[0]
    }
}

class SgetByte(pc: Int, code: CodeItem) : Sget(pc, code) {
    override fun performGet(value: Array<RegisterValue>, memory: Memory) {
        if (value.size != 1) {
            memory.exception = ExceptionValue("SgetByte: Field size is not 1")
            return
        }
        if (value[0] !is RegisterValue.Int) {
            memory.exception = ExceptionValue("SgetByte: Field is not an integer")
            return
        }
        memory.registers[vAA] = value[0]
    }
}

class SgetChar(pc: Int, code: CodeItem) : Sget(pc, code) {
    override fun performGet(value: Array<RegisterValue>, memory: Memory) {
        if (value.size != 1) {
            memory.exception = ExceptionValue("SgetChar: Field size is not 1")
            return
        }
        if (value[0] !is RegisterValue.Int) {
            memory.exception = ExceptionValue("SgetChar: Field is not an integer")
            return
        }
        memory.registers[vAA] = value[0]
    }
}

class SgetShort(pc: Int, code: CodeItem) : Sget(pc, code) {
    override fun performGet(value: Array<RegisterValue>, memory: Memory) {
        if (value.size != 1) {
            memory.exception = ExceptionValue("SgetShort: Field size is not 1")
            return
        }
        if (value[0] !is RegisterValue.Int) {
            memory.exception = ExceptionValue("SgetShort: Field is not an integer")
            return
        }
        memory.registers[vAA] = value[0]
    }
}

class Sput(pc: Int, val code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value = memory.registers[vAA]
        environment.setStaticField(code, KindBBBB, arrayOf(value))
        return pc + insnLength
    }
}

class SputWide(pc: Int, val code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value1 = memory.registers[vAA]
        val value2 = memory.registers[vAA + 1]
        environment.setStaticField(code, KindBBBB, arrayOf(value1, value2))
        return pc + insnLength
    }
}

class SputObject(pc: Int, val code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value = memory.registers[vAA]
        if (value !is RegisterValue.ObjectRef) {
            memory.exception = ExceptionValue("SputObject: Value is not an object reference")
            return pc + insnLength
        }
        environment.setStaticField(code, KindBBBB, arrayOf(value))
        return pc + insnLength
    }
}

class SputBoolean(pc: Int, val code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value = memory.registers[vAA]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("SputBoolean: Value is not an integer")
            return pc + insnLength
        }
        environment.setStaticField(code, KindBBBB, arrayOf(value))
        return pc + insnLength
    }
}

class SputByte(pc: Int, val code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value = memory.registers[vAA]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("SputByte: Value is not an integer")
            return pc + insnLength
        }
        environment.setStaticField(code, KindBBBB, arrayOf(value))
        return pc + insnLength
    }
}

class SputChar(pc: Int, val code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value = memory.registers[vAA]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("SputChar: Value is not an integer")
            return pc + insnLength
        }
        environment.setStaticField(code, KindBBBB, arrayOf(value))
        return pc + insnLength
    }
}

class SputShort(pc: Int, val code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val value = memory.registers[vAA]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("SputShort: Value is not an integer")
            return pc + insnLength
        }
        environment.setStaticField(code, KindBBBB, arrayOf(value))
        return pc + insnLength
    }
}

