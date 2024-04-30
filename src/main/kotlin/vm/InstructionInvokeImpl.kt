package com.yhs0602.vm

import com.yhs0602.dex.CodeItem
import com.yhs0602.dex.DexFile

fun executeInvokePolymorphic(
    pc: Int,
    code: CodeItem,
    contextDexFile: DexFile,
    frame: Frame
): Int {
    val vAA = code.insns[pc + 1].toInt()
    val protoIndex = (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8))
    val protoRef = contextDexFile.protoIds[protoIndex]
    val methodIndex = (code.insns[pc + 4].toInt() or (code.insns[pc + 5].toInt() shl 8))
    val methodRef = contextDexFile.methodIds[methodIndex]
    val argCount = protoRef.parameters.size
    val args = (0 until argCount).map {
        frame.registers[code.insns[pc + 6 + it].toInt()]
    }
    // Invoke
    return pc + 6 + argCount // 4rcc 형식은 6 + argCount 바이트 길이를 가짐
}

fun executeInvokePolymorphicRange(
    pc: Int,
    code: CodeItem,
    contextDexFile: DexFile,
    frame: Frame
): Int {
    val vAA = code.insns[pc + 1].toInt()
    val protoIndex = (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8))
    val protoRef = contextDexFile.protoIds[protoIndex]
    val methodIndex = (code.insns[pc + 4].toInt() or (code.insns[pc + 5].toInt() shl 8))
    val methodRef = contextDexFile.methodIds[methodIndex]
    val argCount = protoRef.parameters.size
    val args = (0 until argCount).map {
        frame.registers[pc + 6 + it]
    }
    // Invoke
    return pc + 6 + argCount // 4rcc 형식은 6 + argCount 바이트 길이를 가짐
}

fun executeInvokeCustom(
    pc: Int,
    code: CodeItem,
    contextDexFile: DexFile,
    frame: Frame
): Int {
    val vAA = code.insns[pc + 1].toInt()
    val callSiteIndex = (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8))
    val callSiteRef = contextDexFile.callSiteIds[callSiteIndex]
    val argCount = callSiteRef.methodHandle.parameters.size
    val args = (0 until argCount).map {
        frame.registers[code.insns[pc + 4 + it].toInt()]
    }
    // Invoke
    return pc + 4 + argCount // 4rcc 형식은 4 + argCount 바이트 길이를 가짐
}

fun executeInvokeCustomRange(
    pc: Int,
    code: CodeItem,
    contextDexFile: DexFile,
    frame: Frame
): Int {
    val vAA = code.insns[pc + 1].toInt()
    val callSiteIndex = (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8))
    val callSiteRef = contextDexFile.callSiteIds[callSiteIndex]
    val argCount = callSiteRef.methodHandle.parameters.size
    val args = (0 until argCount).map {
        frame.registers[pc + 4 + it]
    }
    // Invoke
    return pc + 4 + argCount // 4rcc 형식은 4 + argCount 바이트 길이를 가짐
}

fun executeConstMethodHandle(
    pc: Int,
    code: CodeItem,
    contextDexFile: DexFile,
    frame: Frame
): Int {
    val vAA = code.insns[pc + 1].toInt()
    val methodHandleIndex = (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8))
    val methodHandleRef = contextDexFile.methodHandles[methodHandleIndex]
    frame.registers[vAA] = RegisterValue.MethodHandle(methodHandleRef)
    return pc + 4 // 21c 형식은 4바이트 길이를 가짐
}

fun executeConstMethodType(
    pc: Int,
    code: CodeItem,
    contextDexFile: DexFile,
    frame: Frame
): Int {
    val vAA = code.insns[pc + 1].toInt()
    val protoIndex = (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8))
    val protoRef = contextDexFile.protoIds[protoIndex]
    frame.registers[vAA] = RegisterValue.Proto(protoRef)
    return pc + 4 // 21c 형식은 4바이트 길이를 가짐
}

fun executeInvokeOp(
    pc: Int,
    code: CodeItem,
    contextDexFile: DexFile,
    frame: Frame,
    op: Int
): Int {

}


fun executeInvokeOpRange(
    pc: Int,
    code: CodeItem,
    contextDexFile: DexFile,
    frame: Frame,
    op: Int
): Int {

}



