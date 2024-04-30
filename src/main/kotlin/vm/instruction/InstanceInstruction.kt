package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Frame

class NewInstance(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

class Iget(pc: Int, code: CodeItem) : Instruction._22c(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

class IgetWide(pc: Int, code: CodeItem) : Instruction._22c(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

class IgetObject(pc: Int, code: CodeItem) : Instruction._22c(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

class IgetBoolean(pc: Int, code: CodeItem) : Instruction._22c(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

class IgetByte(pc: Int, code: CodeItem) : Instruction._22c(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

class IgetChar(pc: Int, code: CodeItem) : Instruction._22c(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

class IgetShort(pc: Int, code: CodeItem) : Instruction._22c(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

class Iput(pc: Int, code: CodeItem) : Instruction._22c(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

class IputWide(pc: Int, code: CodeItem) : Instruction._22c(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

class IputObject(pc: Int, code: CodeItem) : Instruction._22c(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

class IputBoolean(pc: Int, code: CodeItem) : Instruction._22c(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

class IputByte(pc: Int, code: CodeItem) : Instruction._22c(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

class IputChar(pc: Int, code: CodeItem) : Instruction._22c(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

class IputShort(pc: Int, code: CodeItem) : Instruction._22c(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        return pc + 1
    }
}

