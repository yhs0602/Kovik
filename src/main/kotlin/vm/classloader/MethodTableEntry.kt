package com.yhs0602.vm.classloader

import com.yhs0602.dex.ProtoId
import java.lang.reflect.Method

data class MethodTableEntry(
    val name: String,
    val protoId: ProtoId,
    val method: Method?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MethodTableEntry

        if (name != other.name) return false
        if (protoId != other.protoId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + protoId.hashCode()
        return result
    }
}