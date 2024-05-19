package com.yhs0602.vm.type

import com.yhs0602.vm.MethodWrapper
import com.yhs0602.vm.classloader.MethodTableEntry
import com.yhs0602.vm.instance.Instance

class PrimitiveType(override val descriptor: String) : Type() {
    override fun isAssignableTo(other: Type): Boolean {
        return this == other
    }

    override val directSuperClass: Type = ObjectType
    override val interfaces: List<Type> = emptyList()

    // no i table for primitive types
    override val interfaceTable: Map<MethodTableEntry, MethodTableEntry> = emptyMap()

    // no v table for primitive types
    // TODO: java.lang.xxx.TYPE
    override val virtualTable: Map<MethodTableEntry, MethodTableEntry> = emptyMap()
    override val methods = emptyMap<MethodTableEntry, MethodWrapper>()
    override val constructors = emptyMap<MethodTableEntry, MethodWrapper>()
    override val staticMethods = emptyMap<MethodTableEntry, MethodWrapper>()
    override fun callClInit() {} // no clinit for primitive types
    override fun createInstance(): Instance? {
        throw UnsupportedOperationException("Cannot create instance of primitive type. InstantiationException")
    }

    override val clazz: Class<*> by lazy {
        when (descriptor) {
            "Z" -> java.lang.Boolean.TYPE
            "B" -> java.lang.Byte.TYPE
            "S" -> java.lang.Short.TYPE
            "C" -> java.lang.Character.TYPE
            "I" -> java.lang.Integer.TYPE
            "J" -> java.lang.Long.TYPE
            "F" -> java.lang.Float.TYPE
            "D" -> java.lang.Double.TYPE
            "V" -> java.lang.Void.TYPE
            else -> throw IllegalArgumentException("Invalid primitive type descriptor: $descriptor")
        }
    }

    companion object {
        val values = listOf(
            "Z", "B", "S", "C", "I", "J", "F", "D", "V"
        )
    }
}