package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Memory

class AddIntLit16(pc: Int, code: CodeItem) : Instruction._22s(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class RsubInt(pc: Int, code: CodeItem) : Instruction._22b(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class MulIntLit16(pc: Int, code: CodeItem) : Instruction._22s(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class DivIntLit16(pc: Int, code: CodeItem) : Instruction._22s(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class RemIntLit16(pc: Int, code: CodeItem) : Instruction._22s(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class AndIntLit16(pc: Int, code: CodeItem) : Instruction._22s(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class OrIntLit16(pc: Int, code: CodeItem) : Instruction._22s(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class XorIntLit16(pc: Int, code: CodeItem) : Instruction._22s(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class AddIntLit8(pc: Int, code: CodeItem) : Instruction._22b(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class RsubIntLit8(pc: Int, code: CodeItem) : Instruction._22b(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class MulIntLit8(pc: Int, code: CodeItem) : Instruction._22b(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class DivIntLit8(pc: Int, code: CodeItem) : Instruction._22b(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class RemIntLit8(pc: Int, code: CodeItem) : Instruction._22b(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class AndIntLit8(pc: Int, code: CodeItem) : Instruction._22b(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class OrIntLit8(pc: Int, code: CodeItem) : Instruction._22b(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class XorIntLit8(pc: Int, code: CodeItem) : Instruction._22b(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class ShlIntLit8(pc: Int, code: CodeItem) : Instruction._22b(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class ShrIntLit8(pc: Int, code: CodeItem) : Instruction._22b(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}

class UshrIntLit8(pc: Int, code: CodeItem) : Instruction._22b(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        return pc + 1
    }
}