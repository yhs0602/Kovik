package com.yhs0602.vm.classloader

import com.yhs0602.dex.ProtoId
import com.yhs0602.vm.MethodWrapper

data class MethodTableEntry(
    val name: String,
    val protoId: ProtoId
)

sealed class Type {

    abstract val directSuperClass: Type?
    abstract val interfaces: List<Type>
    abstract val interfaceTable: Map<MethodTableEntry, MethodTableEntry>
    abstract val virtualTable: Map<MethodTableEntry, MethodTableEntry>
    abstract val descriptor: String
    abstract val methods: Map<MethodTableEntry, MethodWrapper>
    abstract val constructors: Map<MethodTableEntry, MethodWrapper>
    abstract val clazz: Class<*>

    // Use this method to get the method entry in the virtual table
    fun getVirtualMethodEntry(givenEntry: MethodTableEntry): MethodTableEntry {
        return virtualTable[givenEntry] ?: error("Method not found in virtual table")
    }

    fun getInterfaceMethodEntry(givenEntry: MethodTableEntry): MethodTableEntry {
        return interfaceTable[givenEntry] ?: error("Method not found in interface table")
    }

    fun getMethod(methodTableEntry: MethodTableEntry): MethodWrapper {
        // check if requested is a constructor
        if (methodTableEntry.name == "<init>") {
            return constructors[methodTableEntry] ?: error("Constructor not found")
        }
        return methods[methodTableEntry] ?: error("Method not found")
    }

    open fun isAssignableTo(other: Type): Boolean {
        if (this == other)
            return true
        // Check if 'other' is a superclass of this class
        var currentClass: Type? = this
        while (currentClass != null) {
            if (currentClass == other) {
                return true
            }
            currentClass = currentClass.directSuperClass
        }
        // Check if 'other' is an interface that this class or any of its superclasses implement
        currentClass = this
        while (currentClass != null) {
            for (interfaceType in currentClass.interfaces) {
                if (interfaceType == other) {
                    return true
                }
                // Additionally check if 'other' interface is extended by any of the interfaces this class implements
                if (interfaceType.isAssignableTo(other)) {
                    return true
                }
            }
            currentClass = currentClass.directSuperClass
        }
        return false
    }

}