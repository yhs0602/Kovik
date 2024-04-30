package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Frame

class Move8(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vA] = frame.registers[vB]
        return pc + insnLength
    }
}

class Move816(pc: Int, code: CodeItem) : Instruction._22x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vAA] = frame.registers[vBBBB]
        return pc + insnLength
    }
}

class Move1616(pc: Int, code: CodeItem) : Instruction._32x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vAAAA] = frame.registers[vBBBB]
        return pc + insnLength
    }
}

class MoveWide(pc: Int, code: CodeItem) : Instruction._22x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vAA] = frame.registers[vBBBB]
        return pc + insnLength
    }
}

class MoveWide816(pc: Int, code: CodeItem) : Instruction._32x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vAAAA] = frame.registers[vBBBB]
        return pc + insnLength
    }
}

class MoveWide1616(pc: Int, code: CodeItem) : Instruction._22x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vAA] = frame.registers[vBBBB]
        return pc + insnLength
    }
}

class MoveObject(pc: Int, code: CodeItem) : Instruction._22x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vAA] = frame.registers[vBBBB]
        return pc + insnLength
    }
}

class MoveObject816(pc: Int, code: CodeItem) : Instruction._32x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vAAAA] = frame.registers[vBBBB]
        return pc + insnLength
    }
}

class MoveObject1616(pc: Int, code: CodeItem) : Instruction._22x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vAA] = frame.registers[vBBBB]
        return pc + 2
    }
}

class MoveResult(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vAA] = frame.result
        return pc + insnLength
    }
}

class MoveResultWide(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vAA] = frame.result
        return pc + insnLength
    }
}

class MoveResultObject(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vAA] = frame.result
        return pc + insnLength
    }
}

class MoveException(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        frame.registers[vAA] = frame.exception
        return pc + insnLength
    }
}