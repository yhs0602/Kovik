package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Frame

class AddInt(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getRegister(vB)
        val valueB = frame.getRegister(vC)
        frame.setRegister(vA, valueA + valueB)
        return pc + 1
    }
}

class SubInt(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getRegister(vB)
        val valueB = frame.getRegister(vC)
        frame.setRegister(vA, valueA - valueB)
        return pc + 1
    }
}

class MulInt(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getRegister(vB)
        val valueB = frame.getRegister(vC)
        frame.setRegister(vA, valueA * valueB)
        return pc + 1
    }
}

class DivInt(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getRegister(vB)
        val valueB = frame.getRegister(vC)
        frame.setRegister(vA, valueA / valueB)
        return pc + 1
    }
}

class RemInt(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getRegister(vB)
        val valueB = frame.getRegister(vC)
        frame.setRegister(vA, valueA % valueB)
        return pc + 1
    }
}

class AndInt(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getRegister(vB)
        val valueB = frame.getRegister(vC)
        frame.setRegister(vA, valueA and valueB)
        return pc + 1
    }
}

class OrInt(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getRegister(vB)
        val valueB = frame.getRegister(vC)
        frame.setRegister(vA, valueA or valueB)
        return pc + 1
    }
}

class XorInt(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getRegister(vB)
        val valueB = frame.getRegister(vC)
        frame.setRegister(vA, valueA xor valueB)
        return pc + 1
    }
}

class ShlInt(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getRegister(vB)
        val valueB = frame.getRegister(vC)
        frame.setRegister(vA, valueA shl valueB)
        return pc + 1
    }
}

class ShrInt(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getRegister(vB)
        val valueB = frame.getRegister(vC)
        frame.setRegister(vA, valueA shr valueB)
        return pc + 1
    }
}

class UshrInt(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getRegister(vB)
        val valueB = frame.getRegister(vC)
        frame.setRegister(vA, valueA ushr valueB)
        return pc + 1
    }
}

class AddLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getRegisterWide(vB)
        val valueB = frame.getRegisterWide(vC)
        frame.setRegisterWide(vA, valueA + valueB)
        return pc + 1
    }
}

class SubLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getRegisterWide(vB)
        val valueB = frame.getRegisterWide(vC)
        frame.setRegisterWide(vA, valueA - valueB)
        return pc + 1
    }
}

class MulLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getRegisterWide(vB)
        val valueB = frame.getRegisterWide(vC)
        frame.setRegisterWide(vA, valueA * valueB)
        return pc + 1
    }
}

class DivLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getRegisterWide(vB)
        val valueB = frame.getRegisterWide(vC)
        frame.setRegisterWide(vA, valueA / valueB)
        return pc + 1
    }
}

class RemLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getRegisterWide(vB)
        val valueB = frame.getRegisterWide(vC)
        frame.setRegisterWide(vA, valueA % valueB)
        return pc + 1
    }
}

class AndLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getRegisterWide(vB)
        val valueB = frame.getRegisterWide(vC)
        frame.setRegisterWide(vA, valueA and valueB)
        return pc + 1
    }
}

class OrLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getRegisterWide(vB)
        val valueB = frame.getRegisterWide(vC)
        frame.setRegisterWide(vA, valueA or valueB)
        return pc + 1
    }
}

class XorLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getRegisterWide(vB)
        val valueB = frame.getRegisterWide(vC)
        frame.setRegisterWide(vA, valueA xor valueB)
        return pc + 1
    }
}

class ShlLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getRegisterWide(vB)
        val valueB = frame.getRegister(vC)
        frame.setRegisterWide(vA, valueA shl valueB)
        return pc + 1
    }
}

class ShrLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getRegisterWide(vB)
        val valueB = frame.getRegister(vC)
        frame.setRegisterWide(vA, valueA shr valueB)
        return pc + 1
    }
}

class UshrLong(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getRegisterWide(vB)
        val valueB = frame.getRegister(vC)
        frame.setRegisterWide(vA, valueA ushr valueB)
        return pc + 1
    }
}

class AddFloat(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getFRegister(vB)
        val valueB = frame.getFRegister(vC)
        frame.setFRegister(vA, valueA + valueB)
        return pc + 1
    }
}

class SubFloat(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getFRegister(vB)
        val valueB = frame.getFRegister(vC)
        frame.setFRegister(vA, valueA - valueB)
        return pc + 1
    }
}

class MulFloat(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getFRegister(vB)
        val valueB = frame.getFRegister(vC)
        frame.setFRegister(vA, valueA * valueB)
        return pc + 1
    }
}

class DivFloat(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getFRegister(vB)
        val valueB = frame.getFRegister(vC)
        frame.setFRegister(vA, valueA / valueB)
        return pc + 1
    }
}

class RemFloat(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getFRegister(vB)
        val valueB = frame.getFRegister(vC)
        frame.setFRegister(vA, valueA % valueB)
        return pc + 1
    }
}

class AddDouble(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getDRegister(vB)
        val valueB = frame.getDRegister(vC)
        frame.setDRegister(vA, valueA + valueB)
        return pc + 1
    }
}

class SubDouble(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getDRegister(vB)
        val valueB = frame.getDRegister(vC)
        frame.setDRegister(vA, valueA - valueB)
        return pc + 1
    }
}

class MulDouble(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getDRegister(vB)
        val valueB = frame.getDRegister(vC)
        frame.setDRegister(vA, valueA * valueB)
        return pc + 1
    }
}

class DivDouble(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getDRegister(vB)
        val valueB = frame.getDRegister(vC)
        frame.setDRegister(vA, valueA / valueB)
        return pc + 1
    }
}

class RemDouble(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val (vA, vB, vC) = code.getABC()
        val valueA = frame.getDRegister(vB)
        val valueB = frame.getDRegister(vC)
        frame.setDRegister(vA, valueA % valueB)
        return pc + 1
    }
}