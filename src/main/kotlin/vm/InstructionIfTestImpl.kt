package com.yhs0602.vm

import com.yhs0602.dex.CodeItem

object CmpOpcode {
    const val EQ = 0x32
    const val NE = 0x33
    const val LT = 0x34
    const val GE = 0x35
    const val GT = 0x36
    const val LE = 0x37
    const val EQZ = 0x38
    const val NEZ = 0x39
    const val LTZ = 0x3a
    const val GEZ = 0x3b
    const val GTZ = 0x3c
    const val LEZ = 0x3D
}

fun executeIfTest(
    pc: Int,
    code: CodeItem,
    frame: Frame,
    kind: Int
): Int {
    val vA = code.insns[pc + 1].toInt()
    val vB = code.insns[pc + 2].toInt()
    val vC = code.insns[pc + 3].toInt()
    val valueB = frame.registers[vB]
    val valueC = frame.registers[vC]
    val result = when (kind) {
        CmpOpcode.EQ -> {
            valueB == valueC
        }

        CmpOpcode.NE -> {
            valueB != valueC
        }

        CmpOpcode.LT -> {
            valueB < valueC
        }

        CmpOpcode.GE -> {
            valueB >= valueC
        }

        CmpOpcode.GT -> {
            valueB > valueC
        }

        CmpOpcode.LE -> {
            valueB <= valueC
        }

        else -> error("Invalid opcode")
    }
    if (result) {
        return pc + code.insns[pc + 4].toInt()
    } else {
        return pc + 5 // 22t 형식은 5바이트 길이를 가짐
    }
}

fun executeIfTestZ(
    pc: Int,
    code: CodeItem,
    frame: Frame,
    kind: Int
): Int {
    val vA = code.insns[pc + 1].toInt()
    val vB = code.insns[pc + 2].toInt()
    val valueB = frame.registers[vB]
    val result = when (kind) {
        CmpOpcode.EQZ -> {
            valueB == RegisterValue.Int(0)
        }

        CmpOpcode.NEZ -> {
            valueB != RegisterValue.Int(0)
        }

        CmpOpcode.LTZ -> {
            valueB < RegisterValue.Int(0)
        }

        CmpOpcode.GEZ -> {
            valueB >= RegisterValue.Int(0)
        }

        CmpOpcode.GTZ -> {
            valueB > RegisterValue.Int(0)
        }

        CmpOpcode.LEZ -> {
            valueB > RegisterValue.Int(0)
        }
    }
    if (result) {
        return pc + code.insns[pc + 4].toInt()
    } else {
        return pc + 5 // 22t 형식은 5바이트 길이를 가짐
    }
}


