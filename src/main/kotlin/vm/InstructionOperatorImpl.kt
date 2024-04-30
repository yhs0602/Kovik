@file:OptIn(ExperimentalUnsignedTypes::class)

package com.yhs0602.vm

import com.yhs0602.dex.CodeItem

fun executeUnaryOp(
    pc: Int,
    code: CodeItem,
    frame: Frame,
    kind: Int
): Int {
    val vA = code.insns[pc + 1].toInt()
    val vB = code.insns[pc + 2].toInt()
    val valueB = frame.registers[vB]
    val result = when (kind) {
        0 -> {
            // neg-int
            if (valueB !is RegisterValue.Int) {
                error("Invalid unary operation")
            }
            -valueB.value
        }

        1 -> {
            // not-int
            if (valueB !is RegisterValue.Int) {
                error("Invalid unary operation")
            }
            valueB.value.inv()
        }

        2 -> {
            // neg-long
            if (valueB !is RegisterValue.Int) {
                error("Invalid unary operation")
            }
            -valueB.value
        }

        3 -> {
            // not-long
            if (valueB !is RegisterValue.Int) {
                error("Invalid unary operation")
            }
            valueB.value.inv()
        }

        4 -> {
            // neg-float
            if (valueB !is RegisterValue.Int) {
                error("Invalid unary operation")
            }
            -valueB.value
        }

        5 -> {
            // neg-double
            if (valueB !is RegisterValue.Int) {
                error("Invalid unary operation")
            }
            -valueB.value
        }

        else -> error("Invalid kind")
    }
    frame.registers[vA] = RegisterValue.Int(result)
    return pc + 3 // 12x 형식은 3바이트 길이를 가짐
}

fun executeBinaryOpABC(
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
        0 -> {
            // add-int
            if (valueB !is RegisterValue.Int || valueC !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value + valueC.value
        }

        1 -> {
            // sub-int
            if (valueB !is RegisterValue.Int || valueC !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value - valueC.value
        }

        2 -> {
            // mul-int
            if (valueB !is RegisterValue.Int || valueC !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value * valueC.value
        }

        3 -> {
            // div-int
            if (valueB !is RegisterValue.Int || valueC !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value / valueC.value
        }

        4 -> {
            // rem-int
            if (valueB !is RegisterValue.Int || valueC !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value % valueC.value
        }

        5 -> {
            // and-int
            if (valueB !is RegisterValue.Int || valueC !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value and valueC.value
        }

        6 -> {
            // or-int
            if (valueB !is RegisterValue.Int || valueC !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value or valueC.value
        }

        7 -> {
            // xor-int
            if (valueB !is RegisterValue.Int || valueC !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value xor valueC
        }
    }
    return pc + 4 // 23x 형식은 4바이트 길이를 가짐
}

fun executeBinaryOpAB(
    pc: Int,
    code: CodeItem,
    frame: Frame,
    kind: Int
): Int {
    val vA = code.insns[pc + 1].toInt()
    val vB = code.insns[pc + 2].toInt()
    val valueB = frame.registers[vB]
    val result = when (kind) {
        0 -> {
            // add-int/2addr
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value + valueB.value
        }

        1 -> {
            // sub-int/2addr
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value - valueB.value
        }

        2 -> {
            // mul-int/2addr
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value * valueB.value
        }

        3 -> {
            // div-int/2addr
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value / valueB.value
        }

        4 -> {
            // rem-int/2addr
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value % valueB.value
        }

        5 -> {
            // and-int/2addr
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value and valueB.value
        }

        6 -> {
            // or-int/2addr
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value or valueB.value
        }

        7 -> {
            // xor-int/2addr
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value xor valueB.value
        }
    }
    frame.registers[vA] = RegisterValue.Int(result)
    return pc + 3 // 12x 형식은 3바이트 길이를 가짐
}

fun executeBinaryOpABCLiteral16(
    pc: Int,
    code: CodeItem,
    frame: Frame,
    kind: Int
): Int {
    val vA = code.insns[pc + 1].toInt()
    val vB = code.insns[pc + 2].toInt()
    val vC = code.insns[pc + 3].toInt()
    val valueB = frame.registers[vB]
    val valueC = code.insns[pc + 4].toShort()
    val result = when (kind) {
        0 -> {
            // add-int/lit16
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value + valueC
        }

        1 -> {
            // rsub-int/lit16
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueC - valueB.value
        }

        2 -> {
            // mul-int/lit16
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value * valueC
        }

        3 -> {
            // div-int/lit16
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value / valueC
        }

        4 -> {
            // rem-int/lit16
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value % valueC
        }

        5 -> {
            // and-int/lit16
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value and valueC.toInt()
        }

        6 -> {
            // or-int/lit16
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value or valueC.toInt()
        }

        7 -> {
            // xor-int/lit16
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value xor valueC.toInt()
        }
    }
    frame.registers[vA] = RegisterValue.Int(result)
    return pc + 5 // 22s 형식은 5바이
}

fun executeBinaryOpABCLiteral8(
    pc: Int,
    code: CodeItem,
    frame: Frame,
    kind: Int
): Int {
    val vA = code.insns[pc + 1].toInt()
    val vB = code.insns[pc + 2].toInt()
    val vC = code.insns[pc + 3].toInt()
    val valueB = frame.registers[vB]
    val valueC = code.insns[pc + 4].toByte()
    val result = when (kind) {
        0 -> {
            // add-int/lit8
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value + valueC
        }

        1 -> {
            // rsub-int/lit8
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueC - valueB.value
        }

        2 -> {
            // mul-int/lit8
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value * valueC
        }

        3 -> {
            // div-int/lit8
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value / valueC
        }

        4 -> {
            // rem-int/lit8
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value % valueC
        }

        5 -> {
            // and-int/lit8
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value and valueC.toInt()
        }

        6 -> {
            // or-int/lit8
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value or valueC.toInt()
        }

        7 -> {
            // xor-int/lit8
            if (valueB !is RegisterValue.Int) {
                error("Invalid binary operation")
            }
            valueB.value xor valueC.toInt()
        }
    }
    frame.registers[vA] = RegisterValue.Int(result)
    return pc + 5 // 22b 형식은 5바이
}
