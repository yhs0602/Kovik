package com.yhs0602.dex

data class MethodId(val typeId: TypeId, val protoId: ProtoId, val name: String) {
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
}
// membername = Simplename or <Simplename>