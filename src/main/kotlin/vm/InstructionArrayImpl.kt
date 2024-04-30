package com.yhs0602.vm

import com.yhs0602.dex.CodeItem
import com.yhs0602.dex.DexFile

object ArrayOp {
    const val Aget = 0x44
    const val AgetWide = 0x45
    const val AgetObject = 0x46
    const val AgetBoolean = 0x47
    const val AgetByte = 0x48
    const val AgetChar = 0x49
    const val AgetShort = 0x4a
    const val Aput = 0x4b
    const val AputWide = 0x4c
    const val AputObject = 0x4d
    const val AputBoolean = 0x4e
    const val AputByte = 0x4f
    const val AputChar = 0x50
    const val AputShort = 0x51
}

fun executeArrayOp(
    pc: Int,
    code: CodeItem,
    contextDexFile: DexFile,
    frame: Frame,
    op: Int,
): Int {
    when (op) {
        ArrayOp.Aget -> {
            val vA = frame.registers[pc + 1].value
            val vB = frame.registers[pc + 2].value
            val vC = frame.registers[pc + 3].value
            val array = memory.returnValue[vB].value as Array<RegisterValue>
            frame.registers[vA] = array[vC]
        }

        ArrayOp.AgetWide -> {
            val vA = frame.registers[pc + 1].value
            val vB = frame.registers[pc + 2].value
            val vC = frame.registers[pc + 3].value
            val array = memory.returnValue[vB].value as Array<RegisterValue>
            frame.registers[vA] = array[vC]
        }

        ArrayOp.AgetObject -> {
            val vA = frame.registers[pc + 1].value
            val vB = frame.registers[pc + 2].value
            val vC = frame.registers[pc + 3].value
            val array = memory.returnValue[vB].value as Array<RegisterValue>
            frame.registers[vA] = array[vC]
        }

        ArrayOp.AgetBoolean -> {
            val vA = frame.registers[pc + 1].value
            val vB = frame.registers[pc + 2].value
            val vC = frame.registers[pc + 3].value
            val array = memory.returnValue[vB].value as Array<RegisterValue>
            frame.registers[vA] = array[vC]
        }

        ArrayOp.AgetByte -> {
            val vA = frame.registers[pc + 1].value
            val vB = frame.registers[pc + 2].value
            val vC = frame.registers[pc + 3].value
            val array = memory.returnValue[vB].value as Array<RegisterValue>
            frame.registers[vA] = array[vC]
        }

        ArrayOp.AgetChar -> {
            val vA = frame.registers[pc + 1].value
            val vB = frame.registers[pc + 2].value
            val vC = frame.registers[pc + 3].value
            val array = memory.returnValue[vB].value as Array<RegisterValue
        }
    }
    return pc + 1
}
