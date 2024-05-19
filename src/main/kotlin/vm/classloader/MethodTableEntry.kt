package com.yhs0602.vm.classloader

import com.yhs0602.dex.ProtoId
import com.yhs0602.dex.TypeId
import java.lang.reflect.Method

data class MethodTableEntry(
    val name: String,
    val parameters: List<TypeId>,
    val method: Method?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MethodTableEntry

        if (name != other.name) return false
        if (parameters != other.parameters) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + parameters.hashCode()
        return result
    }
}