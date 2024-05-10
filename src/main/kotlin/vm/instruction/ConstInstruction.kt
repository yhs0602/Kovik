package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Environment
import com.yhs0602.vm.Memory
import com.yhs0602.vm.RegisterValue

class Const4(pc: Int, code: CodeItem) : Instruction._11n(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        memory.registers[vA] = RegisterValue.Int(LB)
        return pc + insnLength
    }

    override fun toString(): String {
        return "Const/4 reg[$vA] <- $LB"
    }
}

class Const16(pc: Int, code: CodeItem) : Instruction._21s(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        memory.registers[vAA] = RegisterValue.Int(LBBBB)
        return pc + insnLength
    }

    override fun toString(): String {
        return "Const/16 reg[$vAA] <- $LBBBB"
    }
}

class Const32(pc: Int, code: CodeItem) : Instruction._31i(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        memory.registers[vAA] = RegisterValue.Int(BBBBhi shl 16 or BBBBlo)
        return pc + insnLength
    }
}

class ConstHigh16(pc: Int, code: CodeItem) : Instruction._21h(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        memory.registers[vAA] = RegisterValue.Int(LBBBB shl 16)
        return pc + insnLength
    }
}

class ConstWide16(pc: Int, code: CodeItem) : Instruction._21s(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        memory.registers[vAA] = RegisterValue.Int(LBBBB)
        memory.registers[vAA + 1] = RegisterValue.Int(0)
        return pc + insnLength
    }

    override fun toString(): String {
        return "ConstWide/16 reg[$vAA] <- $LBBBB"
    }
}

class ConstWide32(pc: Int, code: CodeItem) : Instruction._31i(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        memory.registers[vAA] = RegisterValue.Int(BBBBhi shl 16 or BBBBlo)
        memory.registers[vAA + 1] = RegisterValue.Int(0)
        return pc + insnLength
    }

    override fun toString(): String {
        return "ConstWide/32 reg[$vAA] <- ${BBBBhi shl 16 or BBBBlo}"
    }
}

class ConstWide64(pc: Int, code: CodeItem) : Instruction._51l(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        memory.registers[vAA] = RegisterValue.Int(BBBBhiii shl 16 or BBBBlo)
        memory.registers[vAA + 1] = RegisterValue.Int(BBBBhi shl 16 or BBBBhii)
        return pc + insnLength
    }

    override fun toString(): String {
        return "ConstWide/64 reg[$vAA] <- bbbbhiii = $BBBBhiii bbbbhii = $BBBBhii bbbblo = $BBBBlo bbbbhi = $BBBBhi"
    }
}

class ConstWideHigh16(pc: Int, code: CodeItem) : Instruction._21h(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        memory.registers[vAA] = RegisterValue.Int(LBBBB shl 16)
        memory.registers[vAA + 1] = RegisterValue.Int(0)
        return pc + insnLength
    }
}

class ConstString(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        memory.registers[vAA] = RegisterValue.StringRef(KindBBBB)
        return pc + insnLength
    }

    override fun toString(): String {
        return "ConstString reg[$vAA] <- Strings[$KindBBBB]"
    }
}

class ConstStringJumbo(pc: Int, code: CodeItem) : Instruction._31c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        memory.registers[vAA] = RegisterValue.StringRef(KindBBBBhi shl 16 or KindBBBBlo)
        return pc + insnLength
    }
}

class ConstClass(pc: Int, code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment, depth: Int): Int {
        memory.registers[vAA] = RegisterValue.ClassRef(KindBBBB)
        return pc + insnLength
    }
}