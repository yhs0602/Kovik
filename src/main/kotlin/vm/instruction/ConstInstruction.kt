package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Frame
import com.yhs0602.vm.Memory
import com.yhs0602.vm.RegisterValue

class Const4(pc: Int, code: CodeItem) : Instruction._11n(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        frame.registers[vA] = RegisterValue.Int(LB)
        return pc + insnLength
    }
}

class Const16(pc: Int, code: CodeItem) : Instruction._21s(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        frame.registers[vAA] = RegisterValue.Int(LBBBB)
        return pc + insnLength
    }
}

class Const32(pc: Int, code: CodeItem) : Instruction._31i(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        frame.registers[vAA] = RegisterValue.Int(BBBBhi shl 16 or BBBBlo)
        return pc + insnLength
    }
}

class ConstHigh16(pc: Int, code: CodeItem) : Instruction._21h(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        frame.registers[vAA] = RegisterValue.Int(LBBBB shl 16)
        return pc + insnLength
    }
}

class ConstWide16(pc: Int, code: CodeItem) : Instruction._21s(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        frame.registers[vAA] = RegisterValue.Int(LBBBB)
        frame.registers[vAA + 1] = RegisterValue.Int(0)
        return pc + insnLength
    }
}

class ConstWide32(pc: Int, code: CodeItem) : Instruction._31i(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        frame.registers[vAA] = RegisterValue.Int(BBBBhi shl 16 or BBBBlo)
        frame.registers[vAA + 1] = RegisterValue.Int(0)
        return pc + insnLength
    }
}

class ConstWide64(pc: Int, code: CodeItem) : Instruction._51l(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        // FIXME
        frame.registers[vAA] = RegisterValue.Int(BBBBhi shl 16 or BBBBlo)
        frame.registers[vAA + 1] = RegisterValue.Int(BBBBhii shl 16 or BBBBhiii)
        return pc + insnLength
    }
}

class ConstWideHigh16(pc: Int, code: CodeItem) : Instruction._21h(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        frame.registers[vAA] = RegisterValue.Int(LBBBB shl 16)
        frame.registers[vAA + 1] = RegisterValue.Int(0)
        return pc + insnLength
    }
}

class ConstString(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        frame.registers[vAA] = RegisterValue.StringRef(KindBBBB)
        return pc + insnLength
    }
}

class ConstStringJumbo(pc: Int, code: CodeItem) : Instruction._31c(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        frame.registers[vAA] = RegisterValue.StringRef(KindBBBBhi shl 16 or KindBBBBlo)
        return pc + insnLength
    }
}

class ConstClass(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, frame: Frame, memory: Memory): Int {
        frame.registers[vAA] = RegisterValue.ClassRef(KindBBBB)
        return pc + insnLength
    }
}