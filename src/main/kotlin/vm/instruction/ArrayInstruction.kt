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
        memory.returnValue = arrayOf(arrayRef)
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
        memory.returnValue = arrayOf(arrayRef)
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


open class Aget(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val index = memory.registers[vCC]
        val arrayRef = memory.registers[vBB]
        if (arrayRef !is RegisterValue.ArrayRef) {
            memory.exception = ExceptionValue("Aget: Not an array reference")
            return pc + insnLength
        }
        if (index !is RegisterValue.Int) {
            memory.exception = ExceptionValue("Aget: Not an integer")
            return pc + insnLength
        }
        if (index.value < 0 || index.value >= arrayRef.length) {
            memory.exception = ExceptionValue("Aget: Index out of bounds")
            return pc + insnLength
        }
        performGet(memory, arrayRef, index)
        return pc + insnLength
    }

    open fun performGet(memory: Memory, arrayRef: RegisterValue.ArrayRef, index: RegisterValue.Int) {
        memory.registers[vAA] = arrayRef.values[index.value]
    }
}

class AgetWide(pc: Int, code: CodeItem) : Aget(pc, code) {
    override fun performGet(memory: Memory, arrayRef: RegisterValue.ArrayRef, index: RegisterValue.Int) {
        memory.registers[vAA] = arrayRef.values[index.value * 2]
        memory.registers[vAA + 1] = arrayRef.values[index.value * 2 + 1]
    }
}

class AgetObject(pc: Int, code: CodeItem) : Aget(pc, code) {
    override fun performGet(memory: Memory, arrayRef: RegisterValue.ArrayRef, index: RegisterValue.Int) {
        val value = arrayRef.values[index.value]
        if (value !is RegisterValue.ObjectRef) {
            memory.exception = ExceptionValue("AgetObject: Not an object reference")
            return
        }
        memory.registers[vAA] = value
    }
}

class AgetBoolean(pc: Int, code: CodeItem) : Aget(pc, code) {
    override fun performGet(memory: Memory, arrayRef: RegisterValue.ArrayRef, index: RegisterValue.Int) {
        val value = arrayRef.values[index.value]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("AgetBoolean: Not an integer")
            return
        }
        memory.registers[vAA] = RegisterValue.Int(if (value.value != 0) 1 else 0)
    }
}

class AgetByte(pc: Int, code: CodeItem) : Aget(pc, code) {
    override fun performGet(memory: Memory, arrayRef: RegisterValue.ArrayRef, index: RegisterValue.Int) {
        val value = arrayRef.values[index.value]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("AgetByte: Not an integer")
            return
        }
        memory.registers[vAA] = RegisterValue.Int(value.value.toByte().toInt())
    }

}

class AgetChar(pc: Int, code: CodeItem) : Aget(pc, code) {
    override fun performGet(memory: Memory, arrayRef: RegisterValue.ArrayRef, index: RegisterValue.Int) {
        val value = arrayRef.values[index.value]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("AgetChar: Not an integer")
            return
        }
        memory.registers[vAA] = RegisterValue.Int(value.value.toChar().toInt())
    }
}

class AgetShort(pc: Int, code: CodeItem) : Aget(pc, code) {
    override fun performGet(memory: Memory, arrayRef: RegisterValue.ArrayRef, index: RegisterValue.Int) {
        val value = arrayRef.values[index.value]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("AgetShort: Not an integer")
            return
        }
        memory.registers[vAA] = RegisterValue.Int(value.value.toShort().toInt())
    }
}


open class Aput(pc: Int, code: CodeItem) : Instruction._23x(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val arrayRef = memory.registers[vBB]
        val index = memory.registers[vCC]
        if (arrayRef !is RegisterValue.ArrayRef) {
            memory.exception = ExceptionValue("Aput: Not an array reference")
            return pc + insnLength
        }
        if (index !is RegisterValue.Int) {
            memory.exception = ExceptionValue("Aput: Not an integer")
            return pc + insnLength
        }
        if (index.value < 0 || index.value >= arrayRef.length) {
            memory.exception = ExceptionValue("Aput: Index out of bounds")
            return pc + insnLength
        }
        performPut(memory, arrayRef, index)
        return pc + insnLength
    }

    open fun performPut(memory: Memory, arrayRef: RegisterValue.ArrayRef, index: RegisterValue.Int) {
        arrayRef.values[index.value] = memory.registers[vAA]
    }
}

class AputWide(pc: Int, code: CodeItem) : Aput(pc, code) {
    override fun performPut(memory: Memory, arrayRef: RegisterValue.ArrayRef, index: RegisterValue.Int) {
        arrayRef.values[index.value * 2] = memory.registers[vAA]
        arrayRef.values[index.value * 2 + 1] = memory.registers[vAA + 1]
    }
}

class AputObject(pc: Int, code: CodeItem) : Aput(pc, code) {
    override fun performPut(memory: Memory, arrayRef: RegisterValue.ArrayRef, index: RegisterValue.Int) {
        val value = memory.registers[vAA]
        if (value !is RegisterValue.ObjectRef) {
            memory.exception = ExceptionValue("AputObject: Not an object reference")
            return
        }
        arrayRef.values[index.value] = value
    }
}

class AputBoolean(pc: Int, code: CodeItem) : Aput(pc, code) {
    override fun performPut(memory: Memory, arrayRef: RegisterValue.ArrayRef, index: RegisterValue.Int) {
        val value = memory.registers[vAA]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("AputBoolean: Not an integer")
            return
        }
        arrayRef.values[index.value] = RegisterValue.Int(if (value.value != 0) 1 else 0)
    }
}

class AputByte(pc: Int, code: CodeItem) : Aput(pc, code) {
    override fun performPut(memory: Memory, arrayRef: RegisterValue.ArrayRef, index: RegisterValue.Int) {
        val value = memory.registers[vAA]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("AputByte: Not an integer")
            return
        }
        arrayRef.values[index.value] = RegisterValue.Int(value.value.toByte().toInt())
    }
}

class AputChar(pc: Int, code: CodeItem) : Aput(pc, code) {
    override fun performPut(memory: Memory, arrayRef: RegisterValue.ArrayRef, index: RegisterValue.Int) {
        val value = memory.registers[vAA]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("AputChar: Not an integer")
            return
        }
        arrayRef.values[index.value] = RegisterValue.Int(value.value.toChar().toInt())
    }
}

class AputShort(pc: Int, code: CodeItem) : Aput(pc, code) {
    override fun performPut(memory: Memory, arrayRef: RegisterValue.ArrayRef, index: RegisterValue.Int) {
        val value = memory.registers[vAA]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("AputShort: Not an integer")
            return
        }
        arrayRef.values[index.value] = RegisterValue.Int(value.value.toShort().toInt())
    }
}

