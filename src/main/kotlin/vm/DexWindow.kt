package com.yhs0602.vm

import com.yhs0602.dex.*

// Simple wrapper class for dex files
class DexWindow(private val dexFile: DexFile) {
    // Need not even be cached
    fun getString(index: Int): String {
        return dexFile.strings[index]
    }

    fun getTypeId(index: Int): TypeId {
        return dexFile.typeIds[index]
    }

    fun getMethodId(index: Int): MethodId {
        return dexFile.methodIds[index]
    }

    fun getField(index: Int): FieldId {
        return dexFile.fieldIds[index]
    }

    fun getProtoId(index: Int): ProtoId {
        return dexFile.protoIds[index]
    }

    fun getCallSiteId(index: Int): CallSiteId {
        return dexFile.callSiteIds[index]
    }

    fun getMethodHandle(index: Int): MethodHandle {
        return dexFile.methodHandles[index]
    }
}