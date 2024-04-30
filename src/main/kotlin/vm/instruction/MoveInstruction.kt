package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Frame

class Move8(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vA] = frame.registers[vB]
        return pc + 2
    }
}

class Move816(pc: Int, code: CodeItem) : Instruction._22x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vA] = frame.registers[vB]
        return pc + 2
    }
}

class Move1616(pc: Int, code: CodeItem) : Instruction._32x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vA] = frame.registers[vB]
        return pc + 2
    }
}

class MoveWide(pc: Int, code: CodeItem) : Instruction._22x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vA] = frame.registers[vB]
        return pc + 2
    }
}

class MoveWide816(pc: Int, code: CodeItem) : Instruction._32x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vA] = frame.registers[vB]
        return pc + 2
    }
}

class MoveWide1616(pc: Int, code: CodeItem) : Instruction._22x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vA] = frame.registers[vB]
        return pc + 2
    }
}

class MoveObject(pc: Int, code: CodeItem) : Instruction._22x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vA] = frame.registers[vB]
        return pc + 2
    }
}

class MoveObject816(pc: Int, code: CodeItem) : Instruction._32x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vA] = frame.registers[vB]
        return pc + 2
    }
}

class MoveObject1616(pc: Int, code: CodeItem) : Instruction._22x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vA] = frame.registers[vB]
        return pc + 2
    }
}

class MoveResult(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vA] = frame.result
        return pc + 1
    }
}

class MoveResultWide(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vA] = frame.result
        return pc + 1
    }
}

class MoveResultObject(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vA] = frame.result
        return pc + 1
    }
}

class MoveException(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vA] = frame.exception
        return pc + 1
    }
}