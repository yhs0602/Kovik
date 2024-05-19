package com.yhs0602.vm.type

import com.yhs0602.vm.MethodWrapper
import com.yhs0602.vm.classloader.MethodTableEntry
import com.yhs0602.vm.instance.Instance
import java.lang.reflect.Modifier

sealed class Type {
    abstract val directSuperClass: Type?
    abstract val interfaces: List<Type>
    abstract val interfaceTable: Map<MethodTableEntry, MethodTableEntry>
    abstract val virtualTable: Map<MethodTableEntry, MethodTableEntry>
    abstract val descriptor: String
    abstract val methods: Map<MethodTableEntry, MethodWrapper>
    abstract val staticMethods: Map<MethodTableEntry, MethodWrapper>
    abstract val constructors: Map<MethodTableEntry, MethodWrapper>
    abstract val clazz: Class<*>
    abstract fun callClInit()
    abstract fun createInstance(): Instance?

    // Use this method to get the method entry in the virtual table
    fun getVirtualMethodEntry(givenEntry: MethodTableEntry): MethodTableEntry {
        return virtualTable[givenEntry] ?: error(
            "Method $givenEntry not found in virtual table: ${virtualTable.keys.joinToString()}"
        )
    }

    fun getInterfaceMethodEntry(givenEntry: MethodTableEntry): MethodTableEntry {
        return interfaceTable[givenEntry] ?: error("Method $givenEntry not found in interface table")
    }

    fun getDirectMethod(methodTableEntry: MethodTableEntry): MethodWrapper {
        // check if requested is a constructor
        if (methodTableEntry.name == "<init>") {
            return constructors[methodTableEntry    ] ?: error("Constructor not found")
        }
        return methods[methodTableEntry] ?: error("Method $methodTableEntry not found")
    }

    fun getStaticMethod(methodTableEntry: MethodTableEntry): MethodWrapper {
        return staticMethods[methodTableEntry] ?: error("Static method $methodTableEntry not found")
    }

    open fun isAssignableTo(other: Type): Boolean {
        if (this == other)
            return true
        if (this is ArrayType && other is ArrayType) {
            return elementType.isAssignableTo(other.elementType)
        }
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

fun MethodTableEntry.isDefault(): Boolean {
    // Check if the method is a default method in the interface
    if (method == null) return false
    return method.declaringClass.isInterface && !Modifier.isAbstract(method.modifiers) && !Modifier.isStatic(method.modifiers)
}