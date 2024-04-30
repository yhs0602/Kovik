@file:OptIn(ExperimentalUnsignedTypes::class)

package com.yhs0602.vm

import com.yhs0602.dex.CodeItem
import com.yhs0602.dex.DexFile

// VM holds and executes dex files
@OptIn(ExperimentalUnsignedTypes::class)
class VMThread(
    val dexes: List<DexFile>,
    val memory: Memory = Memory()
) {
    var pc = 0
    val strings = dexes.associateWith {
        it.strings
    }

    fun executeMethod(code: CodeItem, contextDexFile: DexFile, argument: Frame, argumentSize: Int) {
        val frame = Frame(code.registersSize.toInt())
        // Copy the argument registers to the frame registers
        for (i in 0 until argumentSize) {
            frame.registers[i] = argument.registers[i]
        }
        while (pc < code.insns.size) {
            val insn = code.insns[pc] and 0xFFu // 명령어 코드
            val operand1 = code.insns[pc].toInt() shr 8 and 0xFF // 피연산자 첫 부분
            println("Executing instruction $insn")
            when (insn.toInt()) {
                0x00 -> { // nop 10x
                    println("nop")
                    pc += 1
                }

                0x01 -> { // move vA, vB 12x
                    val vA = (operand1 and 0x0F) // 하위 4비트가 vA
                    val vB = (operand1 shr 4) // 상위 4비트가 vB
                    frame.registers[vA] = frame.registers[vB]
                    pc += 1 // 12x 형식은 1 short 길이를 가짐
                }

                0x02 -> { // move/from16 vAA, vBBBB 22x: AA|op BBBB
                    val vAA = operand1
                    val vBBBB = code.insns[pc + 1].toInt()
                    frame.registers[vAA] = frame.registers[vBBBB]
                    pc += 2 // 22x 형식은 2 short 길이를 가짐
                }

                0x03 -> { // move/16 vAAAA, vBBBB 32x: 00|op AAAA BBBB
                    val vAAAA = code.insns[pc + 1].toInt()
                    val vBBBB = code.insns[pc + 2].toInt()
                    frame.registers[vAAAA] = frame.registers[vBBBB]
                    pc += 3 // 32x 형식은 3 short 길이를 가짐
                }

                0x04 -> { // move-wide vA, vB 12x: B|A|op
                    val vA = (operand1 and 0x0F) // 하위 4비트가 vA
                    val vB = (operand1 shr 4) // 상위 4비트가 vB
                    val source1 = frame.registers[vB]
                    val source2 = frame.registers[vB + 1]
                    frame.registers[vA] = source1
                    frame.registers[vA + 1] = source2
                    pc += 1 // 12x 형식은 1 short 길이를 가짐
                }

                0x05 -> { // move-wide/from16 vAA, vBBBB 22x: AA|op BBBB
                    val vAA = operand1
                    val vBBBB = code.insns[pc + 1].toInt()
                    val source1 = frame.registers[vBBBB]
                    val source2 = frame.registers[vBBBB + 1]
                    frame.registers[vAA] = source1
                    frame.registers[vAA + 1] = source2
                    pc += 2 // 22x 형식은 2 short 길이를 가짐
                }

                0x06 -> { // move-wide/16 vAAAA, vBBBB 32x: 00|op AAAA BBBB
                    val vAAAA = code.insns[pc + 1].toInt()
                    val vBBBB = code.insns[pc + 2].toInt()
                    val source1 = frame.registers[vBBBB]
                    val source2 = frame.registers[vBBBB + 1]
                    frame.registers[vAAAA] = source1
                    frame.registers[vAAAA + 1] = source2
                    pc += 3 // 32x 형식은 3 short 길이를 가짐
                }

                0x07 -> { // move-object vA, vB 12x
                    val vA = (code.insns[pc + 1].toInt() and 0x0F) // 하위 4비트가 vA
                    val vB = (code.insns[pc + 1].toInt() shr 4) // 상위 4비트가 vB
                    frame.registers[vA] = frame.registers[vB]
                    pc += 1 // 12x 형식은 1 short 길이를 가짐
                }

                0x08 -> { // move-object/from16 vAA, vBBBB 22x
                    val vAA = code.insns[pc + 1].toInt()
                    val vBBBB = (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8))
                    frame.registers[vAA] = frame.registers[vBBBB]
                    pc += 4 // 22x 형식은 4바이트 길이를 가짐
                }

                0x09 -> { // move-object/16 vAAAA, vBBBB 32x
                    val vAAAA = (code.insns[pc + 1].toInt() or (code.insns[pc + 2].toInt() shl 8))
                    val vBBBB = (code.insns[pc + 3].toInt() or (code.insns[pc + 4].toInt() shl 8))
                    frame.registers[vAAAA] = frame.registers[vBBBB]
                    pc += 5 // 32x 형식은 5바이트 길이를 가짐
                }

                0x0A -> { // move-result vAA 11x
                    // 가장 최근 invoke-kind의 단일 워드 비객체 결과를 표시된 레지스터로 이동합니다.
                    // 이 작업은 단일 워드, 비객체 결과가 무시되지 않는 invoke-kind 바로 다음에 명령어 형태로 실행되어야 합니다.
                    // 다른 위치에서는 무효합니다.
                    val vAA = code.insns[pc + 1].toInt()
                    frame.registers[vAA] = memory.returnValue[0]
                    pc += 2 // 11x 형식은 2바이트 길이를 가짐
                }

                0x0B -> { // move-result-wide vAA 11x
                    // 가장 최근 invoke-kind의 두 워드 결과를 표시된 레지스터로 이동합니다.
                    // 이 작업은 두 워드 결과가 무시되지 않는 invoke-kind 바로 다음에 명령어 형태로 실행되어야 합니다.
                    // 다른 위치에서는 무효합니다.
                    val vAA = code.insns[pc + 1].toInt()
                    frame.registers[vAA] = memory.returnValue[0]
                    frame.registers[vAA + 1] = memory.returnValue[1]
                    pc += 2 // 11x 형식은 2바이트 길이를 가짐
                }

                0x0C -> { // move-result-object vAA 11x
                    // 가장 최근 invoke-kind의 객체 결과를 표시된 레지스터로 이동합니다.
                    // 이 작업은 객체 결과가 무시되지 않는 invoke-kind 바로 다음에 명령어 형태로 실행되어야 합니다.
                    // 다른 위치에서는 무효합니다.
                    val vAA = code.insns[pc + 1].toInt()
                    frame.registers[vAA] = memory.returnValue[0]
                    pc += 2 // 11x 형식은 2바이트 길이를 가짐
                }

                0x0D -> { // move-exception vAA 11x
                    // 현재 예외 객체를 표시된 레지스터로 이동합니다.
                    // 이 작업은 catch 핸들러의 첫 번째 명령어로 실행되어야 합니다.
                    // 다른 위치에서는 무효합니다.
                    val vAA = code.insns[pc + 1].toInt()
                    TODO("Implement exception handling")
                    frame.registers[vAA] = RegisterValue.Int(0)
                    pc += 2 // 11x 형식은 2바이트 길이를 가짐
                }

                0x0E -> { // return-void 10x
                    // 현재 메서드에서 void를 반환합니다.
                    // 이 명령어는 메서드의 마지막 명령어로 실행되어야 합니다.
                    // 다른 위치에서는 무효합니다.
                    return
                }

                0x0F -> { // return vAA 11x
                    // vAA 레지스터의 값을 현재 메서드에서 반환합니다.
                    // 이 명령어는 메서드의 마지막 명령어로 실행되어야 합니다.
                    // 다른 위치에서는 무효합니다.
                    val vAA = code.insns[pc + 1].toInt()
                    memory.returnValue = listOf(frame.registers[vAA])
                    return
                }

                0x10 -> { // return-wide vAA 11x
                    // vAA 레지스터의 값을 현재 메서드에서 반환합니다.
                    // 이 명령어는 메서드의 마지막 명령어로 실행되어야 합니다.
                    // 다른 위치에서는 무효합니다.
                    val vAA = code.insns[pc + 1].toInt()
                    memory.returnValue = listOf(frame.registers[vAA], frame.registers[vAA + 1])
                    return
                }

                0x11 -> { // return-object vAA 11x
                    // vAA 레지스터의 값을 현재 메서드에서 반환합니다.
                    // 이 명령어는 메서드의 마지막 명령어로 실행되어야 합니다.
                    // 다른 위치에서는 무효합니다.
                    val vAA = code.insns[pc + 1].toInt()
                    memory.returnValue = listOf(frame.registers[vAA])
                    return
                }

                0x12 -> { // const/4 vA, #+B 11n
                    // 제공된 리터럴 값(32비트로 부호 확장됨)을 지정된 레지스터로 이동합니다.
                    // vA 레지스터에 0부터 15까지의 값을 설정합니다.
                    val vA = (code.insns[pc + 1].toInt() and 0x0F) // 하위 4비트가 vA
                    val B = (code.insns[pc + 1].toInt() shr 4) // 상위 4비트가 B
                    frame.registers[vA] = RegisterValue.Int(B)
                    pc += 2 // 11n 형식은 2바이트 길이를 가짐
                }

                0x13 -> { // const/16 vAA, #+BBBB 21s
                    // 제공된 리터럴 값(16비트로 부호 확장됨)을 지정된 레지스터로 이동합니다.
                    // vAA 레지스터에 0부터 255까지의 값을 설정합니다.
                    val vAA = code.insns[pc + 1].toInt()
                    val BBBB = (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8))
                    frame.registers[vAA] = RegisterValue.Int(BBBB)
                    pc += 4 // 21s 형식은 4바이트 길이를 가짐
                }

                0x14 -> { // const/16 vAA, #+BBBBBBBB 31i
                    // 제공된 리터럴 값을 지정된 레지스터로 이동합니다.
                    val vAA = code.insns[pc + 1].toInt()
                    val BBBBBBBB =
                        (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8) or (code.insns[pc + 4].toInt() shl 16) or (code.insns[pc + 5].toInt() shl 24))
                    frame.registers[vAA] = RegisterValue.Int(BBBBBBBB)
                    pc += 6 // 31i 형식은 6바이트 길이를 가짐
                }

                0x15 -> { // const/high16 vAA, #+BBBB0000 21h
                    // 제공된 리터럴 값을 지정된 레지스터로 이동합니다.
                    val vAA = code.insns[pc + 1].toInt()
                    val BBBB = (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8))
                    frame.registers[vAA] = RegisterValue.Int(BBBB shl 16)
                    pc += 4 // 21h 형식은 4바이트 길이를 가짐
                }

                0x16 -> { // const-wide/16 vAA, #+BBBB	21s
                    // 제공된 리터럴 값(64비트로 부호 확장됨)을 지정된 레지스터 쌍으로 이동합니다.
                    val vAA = code.insns[pc + 1].toInt()
                    val BBBB = (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8))
                    frame.registers[vAA] = RegisterValue.Int(BBBB)
                    frame.registers[vAA + 1] = RegisterValue.Int(BBBB)
                }

                0x17 -> { // const-wide/32 vAA, #+BBBBBBBB	31i
                    // 제공된 리터럴 값을 지정된 레지스터 쌍으로 이동합니다.
                    val vAA = code.insns[pc + 1].toInt()
                    val BBBBBBBB =
                        (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8) or (code.insns[pc + 4].toInt() shl 16) or (code.insns[pc + 5].toInt() shl 24))
                    frame.registers[vAA] = RegisterValue.Int(BBBBBBBB)
                    frame.registers[vAA + 1] = RegisterValue.Int(BBBBBBBB)
                    pc += 6 // 31i 형식은 6바이트 길이를 가짐
                }

                0x18 -> { // const-wide vAA, #+BBBBBBBBBBBBBBBB	51l
                    val vAA = code.insns[pc + 1].toInt()
                    val BBBBBBBBBBBBBBBB =
                        (code.insns[pc + 2].toLong() or (code.insns[pc + 3].toLong() shl 8) or (code.insns[pc + 4].toLong() shl 16) or (code.insns[pc + 5].toLong() shl 24) or (code.insns[pc + 6].toLong() shl 32) or (code.insns[pc + 7].toLong() shl 40) or (code.insns[pc + 8].toLong() shl 48) or (code.insns[pc + 9].toLong() shl 56))
                    frame.registers[vAA] = RegisterValue.Int((BBBBBBBBBBBBBBBB and 0xFFFFFFFFL).toInt())
                    frame.registers[vAA + 1] = RegisterValue.Int((BBBBBBBBBBBBBBBB shr 32).toInt())
                }

                0x19 -> { // const-wide/high16 vAA, #+BBBB000000000000	21h
                    val vAA = code.insns[pc + 1].toInt()
                    val BBBB = (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8))
                    frame.registers[vAA] = RegisterValue.Int(BBBB shl 48)
                    frame.registers[vAA + 1] = RegisterValue.Int(BBBB shl 48)
                    pc += 4 // 21h 형식은 4바이트 길이를 가짐
                }

                0x1A -> { // const-string vAA, string@BBBB	21c
                    val vAA = code.insns[pc + 1].toInt()
                    val stringIndex = (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8))
                    val stringRef = strings[contextDexFile]?.get(stringIndex) ?: error("String not found")
                    frame.registers[vAA] = RegisterValue.StringRef(stringRef)
                    pc += 4 // 21c 형식은 4바이트 길이를 가짐
                }

                0x1B -> { // const-string/jumbo vAA, string@BBBBBBBB    31c
                    val vAA = code.insns[pc + 1].toInt()
                    val stringIndex =
                        (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8) or (code.insns[pc + 4].toInt() shl 16) or (code.insns[pc + 5].toInt() shl 24))
                    val stringRef = strings[contextDexFile]?.get(stringIndex) ?: error("String not found")
                    frame.registers[vAA] = RegisterValue.StringRef(stringRef)
                    pc += 6 // 31c 형식은 6바이트 길이를 가짐
                }

                0x1C -> { // const-class vAA, type@BBBB	21c
                    val vAA = code.insns[pc + 1].toInt()
                    val typeIndex = (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8))
                    val typeRef = contextDexFile.typeIds[typeIndex]
                    frame.registers[vAA] = RegisterValue.ClassRef(typeRef)
                    pc += 4 // 21c 형식은 4바이트 길이를 가짐
                }

                0x1D -> { // monitor-enter vAA; 11x
                    // vAA 레지스터가 참조하는 객체의 모니터를 잠급니다.
                    // 이 명령어는 동기화된 블록의 첫 번째 명령어로 실행되어야 합니다.
                    // 다른 위치에서는 무효합니다.
                    val vAA = code.insns[pc + 1].toInt()
                    // TODO: Implement monitor-enter
                    pc += 2 // 11x 형식은 2바이트 길이를 가짐
                }

                0x1E -> { // monitor-exit vAA	11x
                    val vAA = code.insns[pc + 1].toInt()
                    // TODO: Implement monitor-exit
                    pc += 2 // 11x 형식은 2바이트 길이를 가짐
                }

                0x1F -> { // check-cast vAA, type@BBBB	21c
                    pc = executeCheckCast(pc, code, contextDexFile, frame)
                }

                0x20 -> {
                    pc = executeInstanceOf(pc, code, contextDexFile, frame)
                }

                0x21 -> {
                    pc = executeArrayLength(pc, code, contextDexFile, frame)
                }

                0x22 -> {
                    pc = executeNewInstance(pc, code, contextDexFile, frame)
                }

                0x23 -> {
                    pc = executeNewArray(pc, code, contextDexFile, frame)
                }

                0x24 -> {
                    pc = executeFilledNewArray(pc, code, contextDexFile, frame)
                }

                0x25 -> {
                    pc = executeFilledNewArrayRange(pc, code, contextDexFile, frame)
                }

                0x26 -> {
                    pc = executeFillArrayData(pc, code, contextDexFile, frame)
                }

                0x27 -> {
                    pc = executeThrow(pc, code, contextDexFile, frame)
                }

                0x28 -> {
                    pc = executeGoto(pc, code)
                }

                0x29 -> {
                    pc = executeGoto16(pc, code)
                }

                0x2A -> {
                    pc = executeGoto32(pc, code)
                }

                0x2B -> {
                    pc = executePackedSwitch(pc, code)
                }

                0x2C -> {
                    pc = executeSparseSwitch(pc, code)
                }

                in 0x2D..0x31 -> {
                    pc = executeCmp(pc, code, frame, insn.toInt())
                }

                in 0x32..0x37 -> {
                    pc = executeIfTest(pc, code, frame, insn.toInt())
                }

                in 0x38..0x3D -> {
                    pc = executeIfTestZ(pc, code, frame, insn.toInt())
                }

                in 0x44..0x51 -> {
                    pc = executeArrayOp(pc, code, contextDexFile, frame, insn.toInt())
                }

                in 0x52..0x5F -> {
                    pc = executeInstanceOp(pc, code, contextDexFile, frame, insn.toInt())
                }

                in 0x60..0x6D -> {
                    pc = executeStaticOp(pc, code, contextDexFile, frame)
                }

                in 0x6e..0x72 -> {
                    pc = executeInvokeOp(pc, code, contextDexFile, frame)
                }

                in 0x74..0x78 -> {
                    pc = executeInvokeOpRange(pc, code, contextDexFile, frame)
                }

                in 0x7B..0x8F -> {
                    pc = executeUnaryOp(pc, code, frame, insn.toInt())
                }

                in 0x90..0xaf -> {
                    pc = executeBinaryOpABC(pc, code, contextDexFile, frame)
                }

                in 0xB0..0xCF -> {
                    pc = executeBinaryOpAB(pc, code, contextDexFile, frame)
                }

                in 0xD0..0xD7 -> {
                    pc = executeBinaryOpABCLiteral16(pc, code, contextDexFile, frame)
                }

                in 0xD8..0xE2 -> {
                    pc = executeBinaryOpABCLiteral8(pc, code, contextDexFile, frame)
                }

                0xFA -> { // invoke-polymorphic {vC, vD, vE, vF, vG}, meth@BBBB, proto@HHHH
                    pc = executeInvokePolymorphic(pc, code, contextDexFile, frame)
                }

                0xFB -> { // invoke-polymorphic/range {vCCCC .. vNNNN}, meth@BBBB, proto@HHHH
                    pc = executeInvokePolymorphicRange(pc, code, contextDexFile, frame)
                }

                0xFC -> {
                    pc = executeInvokeCustom(pc, code, contextDexFile, frame)
                }

                0xFD -> {
                    pc = executeInvokeCustomRange(pc, code, contextDexFile, frame)
                }

                0xFE -> {
                    pc = executeConstMethodHandle(pc, code, contextDexFile, frame)
                }

                0xFF -> {
                    pc = executeConstMethodType(pc, code, contextDexFile, frame)
                }

                else -> {
                    error("Unknown instruction $insn")
                }
            }
        }
    }
}
