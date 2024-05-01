package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Environment
import com.yhs0602.vm.Memory

class PackedSwitch(pc: Int, code: CodeItem) : Instruction._31t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class SparseSwitch(pc: Int, code: CodeItem) : Instruction._31t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class IfEq(pc: Int, code: CodeItem) : Instruction._22t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class IfNe(pc: Int, code: CodeItem) : Instruction._22t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class IfLt(pc: Int, code: CodeItem) : Instruction._22t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class IfGe(pc: Int, code: CodeItem) : Instruction._22t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class IfGt(pc: Int, code: CodeItem) : Instruction._22t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class IfLe(pc: Int, code: CodeItem) : Instruction._22t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class IfEqz(pc: Int, code: CodeItem) : Instruction._21t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class IfNez(pc: Int, code: CodeItem) : Instruction._21t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class IfLtz(pc: Int, code: CodeItem) : Instruction._21t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class IfGez(pc: Int, code: CodeItem) : Instruction._21t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class IfGtz(pc: Int, code: CodeItem) : Instruction._21t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class IfLez(pc: Int, code: CodeItem) : Instruction._21t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}
