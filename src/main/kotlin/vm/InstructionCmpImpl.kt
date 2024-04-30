package com.yhs0602.vm

import com.yhs0602.dex.CodeItem

fun executeCmp(
    pc: Int,
    code: CodeItem,
    frame: Frame,
    kind: Int
): Int {
    val vAA = code.insns[pc + 1].toInt()
    val vBB = code.insns[pc + 2].toInt()
    val vCC = code.insns[pc + 3].toInt()
    val valueB = frame.registers[vBB]
    val valueC = frame.registers[vCC]
    val result = when (kind) {
        0 -> {
            // cmpl-float
            if (valueB !is RegisterValue.Int || valueC !is RegisterValue.Int) {
                error("Invalid comparison")
            }
            valueB.value < valueC.value
        }

        1 -> {
            // cmpg-float
            if (valueB !is RegisterValue.Int || valueC !is RegisterValue.Int) {
                error("Invalid comparison")
            }
            valueB.value > valueC.value
        }

        2 -> {
            // cmpl-double
            if (valueB !is RegisterValue.Int || valueC !is RegisterValue.Int) {
                error("Invalid comparison")
            }
            valueB.value < valueC.value
        }

        3 -> {
            // cmpg-double
            if (valueB !is RegisterValue.Int || valueC !is RegisterValue.Int) {
                error("Invalid comparison")
            }
            valueB.value > valueC.value
        }

        4 -> {
            // cmp-long
            if (valueB !is RegisterValue.Int || valueC !is RegisterValue.Int) {
                error("Invalid comparison")
            }
            valueB.value < valueC.value
        }

        else -> error("Invalid kind")
    }
    frame.registers[vAA] = RegisterValue.Int(if (result) 1 else 0)
    return pc + 4 // 23x 형식은 4바이트 길이를 가짐
}