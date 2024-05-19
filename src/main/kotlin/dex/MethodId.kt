package com.yhs0602.dex

import com.yhs0602.vm.classloader.MethodTableEntry

data class MethodId(val classId: TypeId, val protoId: ProtoId, val name: String) {
    override fun toString(): String {
        // human readable prototype
        val returnType = protoId.returnType
        val parameterTypes = protoId.parameters
        val humanReadableProto = buildString {
            append(returnType.descriptor)
            append(" ")
            append(name)
            append("(")
            append(parameterTypes.joinToString(", ") {
                it.descriptor
            })
            append(")")
        }
        return humanReadableProto
    }

    fun toMethodTableEntry(): MethodTableEntry {
        return MethodTableEntry(
            name,
            protoId,
            null
        )
    }
}
// membername = Simplename or <Simplename>