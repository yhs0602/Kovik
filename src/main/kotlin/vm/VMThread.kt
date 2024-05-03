@file:OptIn(ExperimentalUnsignedTypes::class)

package com.yhs0602.vm

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.instruction.Instruction

fun executeMethod(
    code: CodeItem,
    environment: Environment,
    argument: Array<RegisterValue>,
    argumentSize: Int,
): Array<RegisterValue> {
    println("New function frame start")
    println("Argument size = $argumentSize Register size = ${code.registersSize} insSize = ${code.insSize} outsSize=${code.outsSize}")
    println("Argument = ${argument.contentToString()}")

    val memory = Memory(code.registersSize.toInt())
    // Copy the argument registers to the frame registers
    var pc = 0
    val registersCount = code.registersSize.toInt()
    for (i in 0 until argumentSize) {
        memory.registers[registersCount - i - 1] = argument[i]
    }

    while (pc < code.insns.size) {
        val instruction = Instruction.fromCode(pc, code)
        environment.beforeInstruction(pc, instruction, memory)
        pc = instruction.execute(pc, memory, environment)
        environment.afterInstruction(pc, instruction, memory)
        if (pc < 0) {
            // return instruction
            break
        }
    }
    return memory.returnValue
}
