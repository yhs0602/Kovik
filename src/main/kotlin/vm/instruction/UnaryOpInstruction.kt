package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Frame

class NegInt(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val value = frame.operandStack.popInt()
        frame.operandStack.pushInt(-value)
        return pc + 1
    }
}

class NotInt(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val value = frame.operandStack.popInt()
        frame.operandStack.pushInt(value.inv())
        return pc + 1
    }
}

class NegLong(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val value = frame.operandStack.popLong()
        frame.operandStack.pushLong(-value)
        return pc + 1
    }
}

class NotLong(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val value = frame.operandStack.popLong()
        frame.operandStack.pushLong(value.inv())
        return pc + 1
    }
}

class NegFloat(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val value = frame.operandStack.popFloat()
        frame.operandStack.pushFloat(-value)
        return pc + 1
    }
}

class NegDouble(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val value = frame.operandStack.popDouble()
        frame.operandStack.pushDouble(-value)
        return pc + 1
    }
}

class IntToLong(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val value = frame.operandStack.popInt()
        frame.operandStack.pushLong(value.toLong())
        return pc + 1
    }
}

class IntToFloat(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val value = frame.operandStack.popInt()
        frame.operandStack.pushFloat(value.toFloat())
        return pc + 1
    }
}

class IntToDouble(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val value = frame.operandStack.popInt()
        frame.operandStack.pushDouble(value.toDouble())
        return pc + 1
    }
}

class LongToInt(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val value = frame.operandStack.popLong()
        frame.operandStack.pushInt(value.toInt())
        return pc + 1
    }
}

class LongToFloat(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val value = frame.operandStack.popLong()
        frame.operandStack.pushFloat(value.toFloat())
        return pc + 1
    }
}

class LongToDouble(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val value = frame.operandStack.popLong()
        frame.operandStack.pushDouble(value.toDouble())
        return pc + 1
    }
}

class FloatToInt(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val value = frame.operandStack.popFloat()
        frame.operandStack.pushInt(value.toInt())
        return pc + 1
    }
}

class FloatToLong(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val value = frame.operandStack.popFloat()
        frame.operandStack.pushLong(value.toLong())
        return pc + 1
    }
}

class FloatToDouble(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val value = frame.operandStack.popFloat()
        frame.operandStack.pushDouble(value.toDouble())
        return pc + 1
    }
}

class DoubleToInt(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val value = frame.operandStack.popDouble()
        frame.operandStack.pushInt(value.toInt())
        return pc + 1
    }
}

class DoubleToLong(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val value = frame.operandStack.popDouble()
        frame.operandStack.pushLong(value.toLong())
        return pc + 1
    }
}

class DoubleToFloat(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val value = frame.operandStack.popDouble()
        frame.operandStack.pushFloat(value.toFloat())
        return pc + 1
    }
}

class IntToByte(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val value = frame.operandStack.popInt()
        frame.operandStack.pushInt(value.toByte().toInt())
        return pc + 1
    }
}

class IntToChar(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val value = frame.operandStack.popInt()
        frame.operandStack.pushInt(value.toChar().toInt())
        return pc + 1
    }
}

class IntToShort(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, frame: Frame): Int {
        val value = frame.operandStack.popInt()
        frame.operandStack.pushInt(value.toShort().toInt())
        return pc + 1
    }
}

