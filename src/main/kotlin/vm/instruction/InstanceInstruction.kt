package com.yhs0602.vm.instruction

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.*

class NewInstance(pc: Int, val code: CodeItem) : Instruction._21c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val typeId = environment.getTypeId(code, KindBBBB)
        val parsedClass = environment.getClassDef(code, typeId)
        if (parsedClass == null) {
            memory.exception = ExceptionValue("NewInstance: Class not found")
            return pc + insnLength
        }
        val obj = environment.createInstance(parsedClass)
        memory.registers[vAA] = RegisterValue.ObjectRef(parsedClass.classDef.typeId, obj)
        return pc + insnLength
    }
}

open class Iget(pc: Int, val code: CodeItem) : Instruction._22c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val obj = memory.registers[vB]
        if (obj !is RegisterValue.ObjectRef) {
            memory.exception = ExceptionValue("Iget: vB is not an object reference")
            return pc + insnLength
        }
        val instance = obj.value
        if (instance == null) {
            memory.exception = ExceptionValue("Iget: object reference is null")
            return pc + insnLength
        }
        performGet(memory, instance)

        return pc + insnLength
    }

    open fun performGet(memory: Memory, instance: Instance) {
        val value = instance.getField(KindCCCC)
        if (value == null) {
            memory.exception = ExceptionValue("Iget: field not found")
            return
        }
        if (value.size != 1) {
            memory.exception = ExceptionValue("Iget: field size is not 1")
            return
        }
        memory.registers[vA] = value[0]
    }
}

class IgetWide(pc: Int, code: CodeItem) : Iget(pc, code) {
    override fun performGet(memory: Memory, instance: Instance) {
        val value = instance.getField(KindCCCC)
        if (value == null) {
            memory.exception = ExceptionValue("IgetWide: field not found")
            return
        }
        if (value.size != 2) {
            memory.exception = ExceptionValue("IgetWide: field size is not 2")
            return
        }
        memory.registers[vA] = value[0]
        memory.registers[vA + 1] = value[1]
    }
}

class IgetObject(pc: Int, code: CodeItem) : Iget(pc, code) {
    override fun performGet(memory: Memory, instance: Instance) {
        val value = instance.getField(KindCCCC)
        if (value == null) {
            memory.exception = ExceptionValue("IgetObject: field not found")
            return
        }
        if (value.size != 1) {
            memory.exception = ExceptionValue("IgetObject: field size is not 1")
            return
        }
        if (value[0] !is RegisterValue.ObjectRef) {
            memory.exception = ExceptionValue("IgetObject: field is not an object reference")
            return
        }
        memory.registers[vA] = value[0]
    }
}

class IgetBoolean(pc: Int, code: CodeItem) : Iget(pc, code) {
    override fun performGet(memory: Memory, instance: Instance) {
        val value = instance.getField(KindCCCC)
        if (value == null) {
            memory.exception = ExceptionValue("IgetBoolean: field not found")
            return
        }
        if (value.size != 1) {
            memory.exception = ExceptionValue("IgetBoolean: field size is not 1")
            return
        }
        if (value[0] !is RegisterValue.Int) {
            memory.exception = ExceptionValue("IgetBoolean: field is not an integer")
            return
        }
        memory.registers[vA] = value[0]
    }
}

class IgetByte(pc: Int, code: CodeItem) : Iget(pc, code) {
    override fun performGet(memory: Memory, instance: Instance) {
        val value = instance.getField(KindCCCC)
        if (value == null) {
            memory.exception = ExceptionValue("IgetByte: field not found")
            return
        }
        if (value.size != 1) {
            memory.exception = ExceptionValue("IgetByte: field size is not 1")
            return
        }
        if (value[0] !is RegisterValue.Int) {
            memory.exception = ExceptionValue("IgetByte: field is not an integer")
            return
        }
        memory.registers[vA] = value[0]
    }
}

class IgetChar(pc: Int, code: CodeItem) : Iget(pc, code) {
    override fun performGet(memory: Memory, instance: Instance) {
        val value = instance.getField(KindCCCC)
        if (value == null) {
            memory.exception = ExceptionValue("IgetChar: field not found")
            return
        }
        if (value.size != 1) {
            memory.exception = ExceptionValue("IgetChar: field size is not 1")
            return
        }
        if (value[0] !is RegisterValue.Int) {
            memory.exception = ExceptionValue("IgetChar: field is not an integer")
            return
        }
        memory.registers[vA] = value[0]
    }
}

class IgetShort(pc: Int, code: CodeItem) : Iget(pc, code) {
    override fun performGet(memory: Memory, instance: Instance) {
        val value = instance.getField(KindCCCC)
        if (value == null) {
            memory.exception = ExceptionValue("IgetShort: field not found")
            return
        }
        if (value.size != 1) {
            memory.exception = ExceptionValue("IgetShort: field size is not 1")
            return
        }
        if (value[0] !is RegisterValue.Int) {
            memory.exception = ExceptionValue("IgetShort: field is not an integer")
            return
        }
        memory.registers[vA] = value[0]
    }
}

open class Iput(pc: Int, code: CodeItem) : Instruction._22c(pc, code) {
    override fun execute(pc: Int, memory: Memory, environment: Environment): Int {
        val obj = memory.registers[vB]
        if (obj !is RegisterValue.ObjectRef) {
            memory.exception = ExceptionValue("Iput: vB is not an object reference")
            return pc + insnLength
        }
        val instance = obj.value
        if (instance == null) {
            memory.exception = ExceptionValue("Iput: object reference is null")
            return pc + insnLength
        }
        performPut(memory, instance)
        return pc + insnLength
    }

    open fun performPut(memory: Memory, instance: Instance) {
        val value = memory.registers[vA]
        instance.setField(KindCCCC, arrayOf(value))
    }
}

class IputWide(pc: Int, code: CodeItem) : Iput(pc, code) {
    override fun performPut(memory: Memory, instance: Instance) {
        val value = memory.registers[vA]
        val value2 = memory.registers[vA + 1]
        instance.setField(KindCCCC, arrayOf(value, value2))
    }
}

class IputObject(pc: Int, code: CodeItem) : Iput(pc, code) {
    override fun performPut(memory: Memory, instance: Instance) {
        val value = memory.registers[vA]
        if (value !is RegisterValue.ObjectRef) {
            memory.exception = ExceptionValue("IputObject: vA is not an object reference")
            return
        }
        instance.setField(KindCCCC, arrayOf(value))
    }
}

class IputBoolean(pc: Int, code: CodeItem) : Iput(pc, code) {
    override fun performPut(memory: Memory, instance: Instance) {
        val value = memory.registers[vA]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("IputBoolean: vA is not an integer")
            return
        }
        instance.setField(KindCCCC, arrayOf(value))
    }
}

class IputByte(pc: Int, code: CodeItem) : Iput(pc, code) {
    override fun performPut(memory: Memory, instance: Instance) {
        val value = memory.registers[vA]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("IputByte: vA is not an integer")
            return
        }
        instance.setField(KindCCCC, arrayOf(value))
    }
}

class IputChar(pc: Int, code: CodeItem) : Iput(pc, code) {
    override fun performPut(memory: Memory, instance: Instance) {
        val value = memory.registers[vA]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("IputChar: vA is not an integer")
            return
        }
        instance.setField(KindCCCC, arrayOf(value))
    }
}

class IputShort(pc: Int, code: CodeItem) : Iput(pc, code) {
    override fun performPut(memory: Memory, instance: Instance) {
        val value = memory.registers[vA]
        if (value !is RegisterValue.Int) {
            memory.exception = ExceptionValue("IputShort: vA is not an integer")
            return
        }
        instance.setField(KindCCCC, arrayOf(value))
    }
}
