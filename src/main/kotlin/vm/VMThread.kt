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
    println("Argument size = $argumentSize Register size = ${code.registersSize} ${code.insSize} ${code.outsSize}")

    val memory = Memory(code.registersSize.toInt())
    // Copy the argument registers to the frame registers
    var pc = 0
    for (i in 0 until argumentSize) {
        memory.registers[i] = argument[i]
    }
    while (pc < code.insns.size) {
        val instruction = Instruction.fromCode(pc, code)
        environment.callback(instruction, memory)
        pc = instruction.execute(pc, memory, environment)
        if (pc < 0) {
            // return instruction
            break
        }
    }
    return memory.returnValue
}
