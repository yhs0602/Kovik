package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Memory

class Sget(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class SgetWide(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class SgetObject(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class SgetBoolean(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class SgetByte(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class SgetChar(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class SgetShort(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class Sput(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class SputWide(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class SputObject(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class SputBoolean(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class SputByte(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class SputChar(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class SputShort(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

