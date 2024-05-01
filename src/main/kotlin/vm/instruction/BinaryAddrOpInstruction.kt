package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Frame
import com.yhs0602.vm.Memory

class AddInt2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVReg(code.vA)
        val v2 = frame.getVReg(code.vB)
        frame.setVReg(code.vA, v1 + v2)
        return pc + 1
    }
}

class SubInt2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVReg(code.vA)
        val v2 = frame.getVReg(code.vB)
        frame.setVReg(code.vA, v1 - v2)
        return pc + 1
    }
}

class MulInt2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVReg(code.vA)
        val v2 = frame.getVReg(code.vB)
        frame.setVReg(code.vA, v1 * v2)
        return pc + 1
    }
}

class DivInt2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVReg(code.vA)
        val v2 = frame.getVReg(code.vB)
        frame.setVReg(code.vA, v1 / v2)
        return pc + 1
    }
}

class RemInt2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVReg(code.vA)
        val v2 = frame.getVReg(code.vB)
        frame.setVReg(code.vA, v1 % v2)
        return pc + 1
    }
}

class AndInt2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVReg(code.vA)
        val v2 = frame.getVReg(code.vB)
        frame.setVReg(code.vA, v1 and v2)
        return pc + 1
    }
}

class OrInt2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVReg(code.vA)
        val v2 = frame.getVReg(code.vB)
        frame.setVReg(code.vA, v1 or v2)
        return pc + 1
    }
}

class XorInt2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVReg(code.vA)
        val v2 = frame.getVReg(code.vB)
        frame.setVReg(code.vA, v1 xor v2)
        return pc + 1
    }
}

class ShlInt2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVReg(code.vA)
        val v2 = frame.getVReg(code.vB)
        frame.setVReg(code.vA, v1 shl v2)
        return pc + 1
    }
}

class ShrInt2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVReg(code.vA)
        val v2 = frame.getVReg(code.vB)
        frame.setVReg(code.vA, v1 shr v2)
        return pc + 1
    }
}

class UshrInt2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVReg(code.vA)
        val v2 = frame.getVReg(code.vB)
        frame.setVReg(code.vA, v1 ushr v2)
        return pc + 1
    }
}

class AddLong2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVRegLong(code.vA)
        val v2 = frame.getVRegLong(code.vB)
        frame.setVRegLong(code.vA, v1 + v2)
        return pc + 1
    }
}

class SubLong2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVRegLong(code.vA)
        val v2 = frame.getVRegLong(code.vB)
        frame.setVRegLong(code.vA, v1 - v2)
        return pc + 1
    }
}

class MulLong2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVRegLong(code.vA)
        val v2 = frame.getVRegLong(code.vB)
        frame.setVRegLong(code.vA, v1 * v2)
        return pc + 1
    }
}

class DivLong2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVRegLong(code.vA)
        val v2 = frame.getVRegLong(code.vB)
        frame.setVRegLong(code.vA, v1 / v2)
        return pc + 1
    }
}

class RemLong2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVRegLong(code.vA)
        val v2 = frame.getVRegLong(code.vB)
        frame.setVRegLong(code.vA, v1 % v2)
        return pc + 1
    }
}

class AndLong2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVRegLong(code.vA)
        val v2 = frame.getVRegLong(code.vB)
        frame.setVRegLong(code.vA, v1 and v2)
        return pc + 1
    }
}

class OrLong2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVRegLong(code.vA)
        val v2 = frame.getVRegLong(code.vB)
        frame.setVRegLong(code.vA, v1 or v2)
        return pc + 1
    }
}

class XorLong2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVRegLong(code.vA)
        val v2 = frame.getVRegLong(code.vB)
        frame.setVRegLong(code.vA, v1 xor v2)
        return pc + 1
    }
}

class ShlLong2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVRegLong(code.vA)
        val v2 = frame.getVReg(code.vB)
        frame.setVRegLong(code.vA, v1 shl v2)
        return pc + 1
    }
}

class ShrLong2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVRegLong(code.vA)
        val v2 = frame.getVReg(code.vB)
        frame.setVRegLong(code.vA, v1 shr v2)
        return pc + 1
    }
}

class UshrLong2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVRegLong(code.vA)
        val v2 = frame.getVReg(code.vB)
        frame.setVRegLong(code.vA, v1 ushr v2)
        return pc + 1
    }
}

class AddFloat2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVRegFloat(code.vA)
        val v2 = frame.getVRegFloat(code.vB)
        frame.setVRegFloat(code.vA, v1 + v2)
        return pc + 1
    }
}

class SubFloat2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVRegFloat(code.vA)
        val v2 = frame.getVRegFloat(code.vB)
        frame.setVRegFloat(code.vA, v1 - v2)
        return pc + 1
    }
}

class MulFloat2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVRegFloat(code.vA)
        val v2 = frame.getVRegFloat(code.vB)
        frame.setVRegFloat(code.vA, v1 * v2)
        return pc + 1
    }
}

class DivFloat2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVRegFloat(code.vA)
        val v2 = frame.getVRegFloat(code.vB)
        frame.setVRegFloat(code.vA, v1 / v2)
        return pc + 1
    }
}

class RemFloat2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVRegFloat(code.vA)
        val v2 = frame.getVRegFloat(code.vB)
        frame.setVRegFloat(code.vA, v1 % v2)
        return pc + 1
    }
}

class AddDouble2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVRegDouble(code.vA)
        val v2 = frame.getVRegDouble(code.vB)
        frame.setVRegDouble(code.vA, v1 + v2)
        return pc + 1
    }
}

class SubDouble2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVRegDouble(code.vA)
        val v2 = frame.getVRegDouble(code.vB)
        frame.setVRegDouble(code.vA, v1 - v2)
        return pc + 1
    }
}

class MulDouble2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVRegDouble(code.vA)
        val v2 = frame.getVRegDouble(code.vB)
        frame.setVRegDouble(code.vA, v1 * v2)
        return pc + 1
    }
}

class DivDouble2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVRegDouble(code.vA)
        val v2 = frame.getVRegDouble(code.vB)
        frame.setVRegDouble(code.vA, v1 / v2)
        return pc + 1
    }
}

class RemDouble2Addr(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        val v1 = frame.getVRegDouble(code.vA)
        val v2 = frame.getVRegDouble(code.vB)
        frame.setVRegDouble(code.vA, v1 % v2)
        return pc + 1
    }
}


