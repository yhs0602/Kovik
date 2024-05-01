@file:OptIn(ExperimentalUnsignedTypes::class)

package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Environment
import com.yhs0602.vm.ExceptionValue
import com.yhs0602.vm.Memory
import com.yhs0602.vm.RegisterValue
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ArrayLength(pc: Int, code: CodeItem) : Instruction._12x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val arrayRef = memory.registers[vB]
        if (arrayRef !is RegisterValue.ArrayRef) {
            memory.exception = ExceptionValue("ArrayLength: Not an array reference")
            return pc + insnLength
        }
        memory.registers[vA] = RegisterValue.Int(arrayRef.length)
        return pc + insnLength
    }
}

class NewArray(pc: Int, val code: CodeItem) : Instruction._22c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val length = memory.registers[vB]
        if (length !is RegisterValue.Int) {
            memory.exception = ExceptionValue("NewArray: Not an integer")
            return pc + insnLength
        }
        val typeId = environment.getTypeId(code, KindCCCC)
        val arrayRef = RegisterValue.ArrayRef(
            typeId,
            length.value,
            arrayOf()
        ) // The array should be initialized later.
        memory.registers[vA] = arrayRef
        return pc + insnLength
    }
}

// This instruction is for creating higher-dimensional arrays.
class FilledNewArray(pc: Int, val code: CodeItem) : Instruction._35c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val length = A
        val typeId = environment.getTypeId(code, BBBB)
        val registerIndices = arrayOf(C, D, E, F, G)
        val array = Array(length) { memory.registers[registerIndices[it]] }
        val arrayRef = RegisterValue.ArrayRef(
            typeId,
            length,
            array
        )
        memory.returnValue = listOf(arrayRef)
        return pc + insnLength
    }
}

class FilledNewArrayRange(pc: Int, val code: CodeItem) : Instruction._3rc(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val length = count
        val typeId = environment.getTypeId(code, BBBB)
        val registerIndices = (CCCC until CCCC + length).toList()
        val array = Array(length) { memory.registers[registerIndices[it]] }
        val arrayRef = RegisterValue.ArrayRef(
            typeId,
            length,
            array
        )
        memory.returnValue = listOf(arrayRef)
        return pc + insnLength
    }
}

class FillArrayData(pc: Int, val code: CodeItem) : Instruction._31t(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val arrayRef = memory.registers[vAA]
        if (arrayRef !is RegisterValue.ArrayRef) {
            memory.exception = ExceptionValue("FillArrayData: Not an array reference")
            return pc + insnLength
        }

        val tableDataSignedBranchOffset = BBBBhi shl 16 or BBBBlo

        // Validate the payload identifier
        val ident = code.insns[tableDataSignedBranchOffset].toInt()
        if (ident != 0x0300) {
            memory.exception = ExceptionValue("FillArrayData: Invalid fill-array-data-payload identifier")
            return pc + insnLength
        }

        val elementWidth = code.insns[tableDataSignedBranchOffset + 1].toInt()
        val numElements =
            code.insns[tableDataSignedBranchOffset + 2].toInt() shl 16 or code.insns[tableDataSignedBranchOffset + 3].toInt()

        val dataStartIndex = tableDataSignedBranchOffset + 4
        val dataEndIndex = dataStartIndex + numElements * (elementWidth / 2)

        // Assuming elementWidth is in bytes, and each element in code.insns is 2 bytes
        var dataIndex = dataStartIndex
        for (i in 0 until numElements) {
            val elementValue = if (elementWidth == 1) {
                // For 1-byte elements, alternate reading from the high and low parts of each ushort
                val byteIndex = dataIndex * 2 + (i % 2)
                if (i % 2 == 0) (code.insns[dataIndex].toInt() and 0xFF00) shr 8
                else {
                    val value = code.insns[dataIndex].toInt() and 0x00FF
                    dataIndex++  // Only increment dataIndex after reading both bytes of the ushort
                    value
                }
            } else {
                // For elements wider than 1 byte
                val value = ByteArray(elementWidth)
                for (byte in 0 until elementWidth) {
                    value[byte] = ((code.insns[dataIndex + byte / 2].toInt() shr ((byte % 2) * 8)) and 0xFF).toByte()
                }
                dataIndex += elementWidth / 2
                ByteBuffer.wrap(value).order(ByteOrder.LITTLE_ENDIAN).int
            }
            arrayRef.values[i] = RegisterValue.Int(elementValue)
        }

        return pc + insnLength
    }
}


class Aget(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AgetWide(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AgetObject(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AgetBoolean(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AgetByte(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AgetChar(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AgetShort(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}


class Aput(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AputWide(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AputObject(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AputBoolean(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AputByte(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AputChar(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

class AputShort(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        return pc + 1
    }
}

