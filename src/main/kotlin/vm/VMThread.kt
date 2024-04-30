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

    fun executeMethod(code: CodeItem, argument: Frame, argumentSize: Int) {
        val frame = Frame(code.registersSize.toInt())
        // Copy the argument registers to the frame registers
        for (i in 0 until argumentSize) {
            frame.registers[i] = argument.registers[i]
        }
        while (pc < code.insns.size) {
            val insn = code.insns[pc]
            println("Executing instruction $insn")
            when (insn.toInt()) {
                0x00 -> { // nop 10x
                    println("nop")
                    pc += 1
                }

                0x01 -> { // move vA, vB 12x
                    val vA = (code.insns[pc + 1].toInt() and 0x0F) // 하위 4비트가 vA
                    val vB = (code.insns[pc + 1].toInt() shr 4) // 상위 4비트가 vB
                    frame.registers[vA] = frame.registers[vB]
                    pc += 2 // 12x 형식은 2바이트 길이를 가짐
                }

                0x02 -> { // move/from16 vAA, vBBBB 22x
                    val vAA = code.insns[pc + 1].toInt()
                    val vBBBB = (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8))
                    frame.registers[vAA] = frame.registers[vBBBB]
                    pc += 4 // 22x 형식은 4바이트 길이를 가짐
                }

                0x03 -> { // move/16 vAAAA, vBBBB 32x
                    val vAAAA = (code.insns[pc + 1].toInt() or (code.insns[pc + 2].toInt() shl 8))
                    val vBBBB = (code.insns[pc + 3].toInt() or (code.insns[pc + 4].toInt() shl 8))
                    frame.registers[vAAAA] = frame.registers[vBBBB]
                    pc += 5 // 32x 형식은 5바이트 길이를 가짐
                }

                0x04 -> { // move-wide vA, vB 12x
                    val vA = (code.insns[pc + 1].toInt() and 0x0F) // 하위 4비트가 vA
                    val vB = (code.insns[pc + 1].toInt() shr 4) // 상위 4비트가 vB
                    val source1 = frame.registers[vB]
                    val source2 = frame.registers[vB + 1]
                    frame.registers[vA] = source1
                    frame.registers[vA + 1] = source2
                    pc += 2 // 12x 형식은 2바이트 길이를 가짐
                }

                0x05 -> { // move-wide/from16 vAA, vBBBB 22x
                    val vAA = code.insns[pc + 1].toInt()
                    val vBBBB = (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8))
                    val source1 = frame.registers[vBBBB]
                    val source2 = frame.registers[vBBBB + 1]
                    frame.registers[vAA] = source1
                    frame.registers[vAA + 1] = source2
                    pc += 4 // 22x 형식은 4바이트 길이를 가짐
                }

                0x06 -> { // move-wide/16 vAAAA, vBBBB 32x
                    val vAAAA = (code.insns[pc + 1].toInt() or (code.insns[pc + 2].toInt() shl 8))
                    val vBBBB = (code.insns[pc + 3].toInt() or (code.insns[pc + 4].toInt() shl 8))
                    val source1 = frame.registers[vBBBB]
                    val source2 = frame.registers[vBBBB + 1]
                    frame.registers[vAAAA] = source1
                    frame.registers[vAAAA + 1] = source2
                    pc += 5 // 32x 형식은 5바이트 길이를 가짐
                }

                0x07 -> { // move-object vA, vB 12x
                    val vA = (code.insns[pc + 1].toInt() and 0x0F) // 하위 4비트가 vA
                    val vB = (code.insns[pc + 1].toInt() shr 4) // 상위 4비트가 vB
                    frame.registers[vA] = frame.registers[vB]
                    pc += 2 // 12x 형식은 2바이트 길이를 가짐
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
                    frame.registers[vAA] = 0 // TODO: Implement exception handling
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
                    memory.returnValue[0] = frame.registers[vAA]
                    return
                }

                0x10 -> { // return-wide vAA 11x
                    // vAA 레지스터의 값을 현재 메서드에서 반환합니다.
                    // 이 명령어는 메서드의 마지막 명령어로 실행되어야 합니다.
                    // 다른 위치에서는 무효합니다.
                    val vAA = code.insns[pc + 1].toInt()
                    memory.returnValue[0] = frame.registers[vAA]
                    memory.returnValue[1] = frame.registers[vAA + 1]
                    return
                }

                0x11 -> { // return-object vAA 11x
                    // vAA 레지스터의 값을 현재 메서드에서 반환합니다.
                    // 이 명령어는 메서드의 마지막 명령어로 실행되어야 합니다.
                    // 다른 위치에서는 무효합니다.
                    val vAA = code.insns[pc + 1].toInt()
                    memory.returnValue[0] = frame.registers[vAA]
                    return
                }

                0x12 -> { // const/4 vA, #+B 11n
                    // 제공된 리터럴 값(32비트로 부호 확장됨)을 지정된 레지스터로 이동합니다.
                    // vA 레지스터에 0부터 15까지의 값을 설정합니다.
                    val vA = (code.insns[pc + 1].toInt() and 0x0F) // 하위 4비트가 vA
                    val B = (code.insns[pc + 1].toInt() shr 4) // 상위 4비트가 B
                    frame.registers[vA] = B
                    pc += 2 // 11n 형식은 2바이트 길이를 가짐
                }

                0x13 -> { // const/16 vAA, #+BBBB 21s
                    // 제공된 리터럴 값(16비트로 부호 확장됨)을 지정된 레지스터로 이동합니다.
                    // vAA 레지스터에 0부터 255까지의 값을 설정합니다.
                    val vAA = code.insns[pc + 1].toInt()
                    val BBBB = (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8))
                    frame.registers[vAA] = BBBB
                    pc += 4 // 21s 형식은 4바이트 길이를 가짐
                }

                0x14 -> { // const/16 vAA, #+BBBBBBBB 31i
                    // 제공된 리터럴 값을 지정된 레지스터로 이동합니다.
                    val vAA = code.insns[pc + 1].toInt()
                    val BBBBBBBB =
                        (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8) or (code.insns[pc + 4].toInt() shl 16) or (code.insns[pc + 5].toInt() shl 24))
                    frame.registers[vAA] = BBBBBBBB
                    pc += 6 // 31i 형식은 6바이트 길이를 가짐
                }

                0x15 -> { // const/high16 vAA, #+BBBB0000 21h
                    // 제공된 리터럴 값을 지정된 레지스터로 이동합니다.
                    val vAA = code.insns[pc + 1].toInt()
                    val BBBB = (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8))
                    frame.registers[vAA] = BBBB shl 16
                    pc += 4 // 21h 형식은 4바이트 길이를 가짐
                }

                0x16 -> { // const-wide/16 vAA, #+BBBB	21s
                    // 제공된 리터럴 값(64비트로 부호 확장됨)을 지정된 레지스터 쌍으로 이동합니다.
                    val vAA = code.insns[pc + 1].toInt()
                    val BBBB = (code.insns[pc + 2].toInt() or (code.insns[pc + 3].toInt() shl 8))
                    frame.registers[vAA] = BBBB
                    frame.registers[vAA + 1] = BBBB
                }
            }
        }
    }
}