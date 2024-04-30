package com.yhs0602.vm

fun executeInstanceOp(
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
            // new-instance
            if (valueC !is RegisterValue.ClassRef) {
                error("Invalid instance operation")
            }
            RegisterValue.ArrayRef(kotlin.Array(valueC.value.size) { RegisterValue.Int(0) })
        }

        1 -> {
            // new-array
            if (valueC !is RegisterValue.Int) {
                error("Invalid instance operation")
            }
            RegisterValue.ArrayRef(kotlin.Array(valueC.value) { RegisterValue.Int(0) })
        }

        2 -> {
            // filled-new-array
            if (valueC !is RegisterValue.ClassRef) {
                error("Invalid instance operation")
            }
            RegisterValue.ArrayRef(kotlin.Array(valueC.value.size) { RegisterValue.Int(0) })
        }

        3 -> {
            // filled-new-array/range
            if (valueC !is RegisterValue.ClassRef) {
                error("Invalid instance operation")
            }
            RegisterValue.ArrayRef(kotlin.Array(valueC.value.size) { RegisterValue.Int(0) })
        }

        else -> error("Invalid kind")
    }
    frame.registers[vA] = result
    return pc + 4 // 35c, 3rc, 35mi, 3rmi 형식은 4바이트 길이를 가짐
} // executeInstanceOp(...

fun executeStaticOp(
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
            // new-instance
            if (valueC !is RegisterValue.ClassRef) {
                error("Invalid static operation")
            }
            RegisterValue.ArrayRef(kotlin.Array(valueC.value.size) { RegisterValue.Int(0) })
        }

        1 -> {
            // new-array
            if (valueC !is RegisterValue.Int) {
                error("Invalid static operation")
            }
            RegisterValue.ArrayRef(kotlin.Array(valueC.value) { RegisterValue.Int(0) })
        }

        2 -> {
            // filled-new-array
            if (valueC !is RegisterValue.ClassRef) {
                error("Invalid static operation")
            }
            RegisterValue.ArrayRef(kotlin.Array(valueC.value.size) { RegisterValue.Int(0) })
        }

        3 -> {
            // filled-new-array/range
            if (valueC !is RegisterValue.ClassRef) {
                error("Invalid static operation")
            }
            RegisterValue.ArrayRef(kotlin.Array(valueC.value.size) { RegisterValue.Int(0) })
        }

        else -> error("Invalid kind")
    }
    frame.registers[vA] = result
    return pc + 4 // 35c, 3rc, 35mi, 3rmi 형식은 4바이트 길이를 가짐
} // executeStaticOp(...
