package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Environment
import com.yhs0602.vm.Memory

class ArrayLength(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class NewArray(pc: Int, code: CodeItem) : Instruction._22c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class FilledNewArray(pc: Int, code: CodeItem) : Instruction._35c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class FilledNewArrayRange(pc: Int, code: CodeItem) : Instruction._3rc(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class FillArrayData(pc: Int, code: CodeItem) : Instruction._31t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class Aget(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AgetWide(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AgetObject(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AgetBoolean(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AgetByte(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AgetChar(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AgetShort(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}


class Aput(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AputWide(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AputObject(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AputBoolean(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AputByte(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AputChar(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AputShort(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

