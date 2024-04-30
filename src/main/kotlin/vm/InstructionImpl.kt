@file:OptIn(ExperimentalUnsignedTypes::class)

package com.yhs0602.vm

import com.yhs0602.dex.CodeItem
import com.yhs0602.dex.DexFile

fun executeCheckCast(
    pc: Int,
    code: CodeItem,
    contextDexFile: DexFile,
    frame: Frame
): Int {
    val vAA = code.insns[pc + 1].toInt()
    val vBBBB = (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8))
    val typeIndex = vBBBB
    val typeRef = contextDexFile.typeIds[typeIndex]
    // Check cast
    val value = frame.registers[vAA]
    when (value) {
        is RegisterValue.Int -> {
            if (typeRef.descriptor != "I") {
                error("Invalid cast")
            }
        }

        is RegisterValue.StringRef -> {
            if (typeRef.descriptor != "Ljava/lang/String;") {
                error("Invalid cast")
            }
        }

        is RegisterValue.ClassRef -> {
            if (typeRef.descriptor != "Ljava/lang/Class;") {
                error("Invalid cast")
            }
        }

        is RegisterValue.ArrayRef -> {
            if (typeRef.descriptor != "[Ljava/lang/Object;") {
                error("Invalid cast")
            }
        }
    }
    return pc + 2 // 21c 형식은 2바이트 길이를 가짐
}

fun executeInstanceOf(
    pc: Int,
    code: CodeItem,
    contextDexFile: DexFile,
    frame: Frame
): Int {
    // FIXME: Implement this well
    val vA = (code.insns[pc + 1].toInt() and 0x0F) // 하위 4비트가 vA, 대상 레지스터
    val vB = (code.insns[pc + 1].toInt() shr 4) // 상위 4비트가 vB
    val vC = (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8)) // vC
    val typeIndex = vC
    val typeRef = contextDexFile.typeIds[typeIndex]
    val value = frame.registers[vB]
    val result = when (value) {
        is RegisterValue.Int -> {
            typeRef.descriptor == "I"
        }

        is RegisterValue.StringRef -> {
            typeRef.descriptor == "Ljava/lang/String;"
        }

        is RegisterValue.ClassRef -> {
            typeRef.descriptor == "Ljava/lang/Class;"
        }
    }
    frame.registers[vA] = RegisterValue.Int(if (result) 1 else 0)
    return pc + 2 // 21c 형식은 2바이트 길이를 가짐
}

fun executeArrayLength(
    pc: Int,
    code: CodeItem,
    contextDexFile: DexFile,
    frame: Frame
): Int {
    val vA = (code.insns[pc + 1].toInt() and 0x0F) // 하위 4비트가 vA
    val vB = (code.insns[pc + 1].toInt() shr 4) // 상위 4비트가 vB
    val arrayRef = frame.registers[vB]
    if (arrayRef !is RegisterValue.ArrayRef) {
        error("Invalid array reference")
    }
    val length = arrayRef.value.size
    frame.registers[vA] = RegisterValue.Int(length)
    return pc + 2 // 12x 형식은 2바이트 길이를 가짐
}

fun executeNewInstance(
    pc: Int,
    code: CodeItem,
    contextDexFile: DexFile,
    frame: Frame
): Int {
    val vAA = code.insns[pc + 1].toInt()
    val typeIndex = (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8))
    val typeRef = contextDexFile.typeIds[typeIndex]
    frame.registers[vAA] = RegisterValue.ClassRef(typeRef)
    return pc + 4 // 22c 형식은 4바이트 길이를 가짐
}

fun executeNewArray(
    pc: Int,
    code: CodeItem,
    contextDexFile: DexFile,
    frame: Frame
): Int {
    val vA = code.insns[pc + 1].toInt()
    val vB = code.insns[pc + 2].toInt()
    val length = frame.registers[vB]
    if (length !is RegisterValue.Int) {
        error("Invalid array length")
    }
    val array = Array<RegisterValue>(length.value) { RegisterValue() }
    frame.registers[vA] = RegisterValue.ArrayRef(array)
    return pc + 2 // 23x 형식은 2바이트 길이를 가짐
}


fun executeFilledNewArray(
    pc: Int,
    code: CodeItem,
    contextDexFile: DexFile,
    frame: Frame
): Int {
    val vAA = code.insns[pc + 1].toInt()
    val typeIndex = (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8))
    val typeRef = contextDexFile.typeIds[typeIndex]
    val argCount = code.insns[pc + 4].toInt()
    val args = (0 until argCount).map {
        frame.registers[code.insns[pc + 5 + it].toInt()]
    }
    val array = Array<RegisterValue>(argCount) { RegisterValue() }
    args.forEachIndexed { index, arg ->
        array[index] = arg
    }
    frame.registers[vAA] = RegisterValue.ArrayRef(array)
    return pc + 5 + argCount // 35c 형식은 5 + argCount 바이트 길이를 가짐
}

fun executeFilledNewArrayRange(
    pc: Int,
    code: CodeItem,
    contextDexFile: DexFile,
    frame: Frame
): Int {
    val vAA = code.insns[pc + 1].toInt()
    val typeIndex = (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8))
    val typeRef = contextDexFile.typeIds[typeIndex]
    val argCount = code.insns[pc + 4].toInt()
    val args = (0 until argCount).map {
        frame.registers[pc + 5 + it]
    }
    val array = Array<RegisterValue>(argCount) { RegisterValue() }
    args.forEachIndexed { index, arg ->
        array[index] = arg
    }
    frame.registers[vAA] = RegisterValue.ArrayRef(array)
    return pc + 5 + argCount // 3rc 형식은 5 + argCount 바이트 길이를 가짐
}

fun executeFillArrayData(
    pc: Int,
    code: CodeItem,
    contextDexFile: DexFile,
    frame: Frame
): Int {
    val arrayRef = frame.registers[code.insns[pc + 1].toInt()]
    if (arrayRef !is RegisterValue.ArrayRef) {
        error("Invalid array reference")
    }
    val elementWidth = code.insns[pc + 2].toInt()
    val size =
        (code.insns[pc + 3].toInt() or (code.insns[pc + 4].toInt() shl 8) or (code.insns[pc + 5].toInt() shl 16) or (code.insns[pc + 6].toInt() shl 24))
    val data = code.insns.sliceArray(pc + 7 until pc + 7 + size * elementWidth)
    val array = Array(size) { RegisterValue() }
    for (i in 0 until size) {
        val start = i * elementWidth
        val end = start + elementWidth
        val value = when (elementWidth) {
            1 -> RegisterValue.Int(data[start].toInt())
            2 -> RegisterValue.Int(data.sliceArray(start until end).toShort().toInt())
            4 -> RegisterValue.Int(data.sliceArray(start until end).toInt())
            else -> error("Invalid element width")
        }
        array[i] = value
    }
    arrayRef.value = array
    return pc + 7 + size * elementWidth // 31t 형식은 7 + size * elementWidth 바이트 길이를 가짐
}

fun executeThrow(
    pc: Int,
    code: CodeItem,
    contextDexFile: DexFile,
    frame: Frame
): Int {
    val vA = code.insns[pc + 1].toInt()
    val exception = frame.registers[vA]
    if (exception !is RegisterValue.ClassRef) {
        error("Invalid exception reference")
    }
    throw Exception(exception.value.descriptor)
}

fun executeGoto(
    pc: Int,
    code: CodeItem,
): Int {
    val offset = (code.insns[pc + 1].toInt() or (code.insns[pc + 2].toInt() shl 8))
    return pc + offset // 10t 형식은 2바이트 길이를 가짐
}

fun executeGoto16(
    pc: Int,
    code: CodeItem,
): Int {
    val offset = (code.insns[pc + 1].toInt() or (code.insns[pc + 2].toInt() shl 8))
    return pc + offset // 20t 형식은 2바이트 길이를 가짐
}

fun executeGoto32(
    pc: Int,
    code: CodeItem,
): Int {
    val offset =
        (code.insns[pc + 1].toInt() or (code.insns[pc + 2].toInt() shl 8) or (code.insns[pc + 3].toInt() shl 16) or (code.insns[pc + 4].toInt() shl 24))
    return pc + offset // 30t 형식은 4바이트 길이를 가짐
}

fun executePackedSwitch(
    pc: Int,
    code: CodeItem,
): Int {
    val vA = code.insns[pc + 1].toInt()
    val offset =
        (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8) or (code.insns[pc + 4].toInt() shl 16) or (code.insns[pc + 5].toInt() shl 24))
    return pc + offset // 31t 형식은 4바이트 길이를 가짐
}

fun executeSparseSwitch(
    pc: Int,
    code: CodeItem,
): Int {
    val vA = code.insns[pc + 1].toInt()
    val offset =
        (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8) or (code.insns[pc + 4].toInt() shl 16) or (code.insns[pc + 5].toInt() shl 24))
    return pc + offset // 31t 형식은 4바이트 길이를 가짐
}

