package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Memory

class Move8(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        memory.registers[vA] = memory.registers[vB]
        return pc + insnLength
    }
}

class Move816(pc: Int, code: CodeItem) : Instruction._22x(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        memory.registers[vAA] = memory.registers[vBBBB]
        return pc + insnLength
    }
}

class Move1616(pc: Int, code: CodeItem) : Instruction._32x(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        memory.registers[vAAAA] = memory.registers[vBBBB]
        return pc + insnLength
    }
}

class MoveWide(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        // 참고: vN에서 vN-1 또는 vN+1 중 하나로 이동하는 것이 허용됩니다.
        // 따라서 구현 시 무언가가 작성되기 전에 레지스터 쌍의 양쪽 모두를 읽도록 해야 합니다.
        val source1 = memory.registers[vB]
        val source2 = memory.registers[vB + 1]
        memory.registers[vA] = source1
        memory.registers[vA + 1] = source2
        return pc + insnLength
    }
}

class MoveWide816(pc: Int, code: CodeItem) : Instruction._22x(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        val source1 = memory.registers[vBBBB]
        val source2 = memory.registers[vBBBB + 1]
        memory.registers[vAA] = source1
        memory.registers[vAA + 1] = source2
        return pc + insnLength
    }
}

class MoveWide1616(pc: Int, code: CodeItem) : Instruction._32x(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        val source1 = memory.registers[vBBBB]
        val source2 = memory.registers[vBBBB + 1]
        memory.registers[vAAAA] = source1
        memory.registers[vAAAA + 1] = source2
        return pc + insnLength
    }
}

class MoveObject(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        memory.registers[vA] = memory.registers[vB]
        return pc + insnLength
    }
}

class MoveObject816(pc: Int, code: CodeItem) : Instruction._22x(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        memory.registers[vAA] = memory.registers[vBBBB]
        return pc + insnLength
    }
}

class MoveObject1616(pc: Int, code: CodeItem) : Instruction._32x(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        memory.registers[vAAAA] = memory.registers[vBBBB]
        return pc + 2
    }
}

class MoveResult(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        memory.registers[vAA] = memory.returnValue[0]
        return pc + insnLength
    }
}

class MoveResultWide(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        memory.registers[vAA] = memory.returnValue[0]
        memory.registers[vAA + 1] = memory.returnValue[1]
        return pc + insnLength
    }
}

class MoveResultObject(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        memory.registers[vAA] = memory.returnValue[0]
        return pc + insnLength
    }
}

class MoveException(pc: Int, code: CodeItem) : Instruction._11x(pc, code) {
    override fun execute(pc: Int, memory: Memory): Int {
        memory.exception?.let {
            memory.registers[vAA] = it
        } ?: run {
            throw IllegalStateException("No exception found")
        }
        return pc + insnLength
    }
}