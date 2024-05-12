package com.yhs0602.vm.classloader

import com.yhs0602.dex.*
import java.lang.reflect.Method

data class MethodTableEntry(
    val name: String,
    val protoId: ProtoId
)

sealed class Type {
    abstract fun isAssignableTo(other: Type): Boolean
    abstract val directSuperClass: Type?
    abstract val interfaces: List<Type>
    abstract val interfaceTable: Map<MethodTableEntry, MethodTableEntry>
    abstract val virtualTable: Map<MethodTableEntry, MethodTableEntry>
    abstract val descriptor: String

    data object Object : Type() {
        override fun isAssignableTo(other: Type): Boolean {
            return true
        }

        override val directSuperClass: Type? = null
        override val interfaces: List<Type> = emptyList()
        override val interfaceTable: Map<MethodTableEntry, MethodTableEntry>
            get() = _interfaceTable
        private val _interfaceTable: MutableMap<MethodTableEntry, MethodTableEntry> = mutableMapOf()
        override val virtualTable: Map<MethodTableEntry, MethodTableEntry>
            get() = _virtualTable
        private val _virtualTable: MutableMap<MethodTableEntry, MethodTableEntry> = mutableMapOf()
        override val descriptor: String = "Ljava/lang/Object;"

        init {
            // populate v-table and i-table of Object
            for (method in java.lang.Object::class.java.methods) {
                val methodId = method.methodTableEntry()
                _virtualTable[methodId] = methodId
            }
            // no i table for Object
        }
    }

    class Primitive(override val descriptor: String) : Type() {
        fun getClass(): Class<*> {
            return when (descriptor) {
                "Z" -> java.lang.Boolean.TYPE
                "B" -> java.lang.Byte.TYPE
                "S" -> java.lang.Short.TYPE
                "C" -> java.lang.Character.TYPE
                "I" -> java.lang.Integer.TYPE
                "J" -> java.lang.Long.TYPE
                "F" -> java.lang.Float.TYPE
                "D" -> java.lang.Double.TYPE
                else -> throw IllegalArgumentException("Invalid primitive type descriptor: $descriptor")
            }
        }

        override fun isAssignableTo(other: Type): Boolean {
            return this == other
        }

        override val directSuperClass: Type = Object
        override val interfaces: List<Type> = emptyList()

        // no i table for primitive types
        override val interfaceTable: Map<MethodTableEntry, MethodTableEntry> = emptyMap()

        // no v table for primitive types
        override val virtualTable: Map<MethodTableEntry, MethodTableEntry> = emptyMap()
    }

    class DexDefinedReference(
        classDef: ParsedClass,
        override val directSuperClass: Type?,
        override val interfaces: List<Type>
    ) : Type() {
        override fun isAssignableTo(other: Type): Boolean {
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

        override val descriptor: String = classDef.classDef.typeId.descriptor
        override val interfaceTable: Map<MethodTableEntry, MethodTableEntry>
            get() = _interfaceTable
        private val _interfaceTable: MutableMap<MethodTableEntry, MethodTableEntry> = mutableMapOf()
        override val virtualTable: Map<MethodTableEntry, MethodTableEntry>
            get() = _virtualTable
        private val _virtualTable: MutableMap<MethodTableEntry, MethodTableEntry> = mutableMapOf()

        init {
            // populate v-table and i-table of the class
            // First copy the super class's v-table and i-table
            if (directSuperClass != null) {
                _interfaceTable.putAll(directSuperClass.interfaceTable)
                _virtualTable.putAll(directSuperClass.virtualTable)
            }
            // update the v-table with the current class's methods
            classDef.classData?.let {
                for (method in it.virtualMethods) {
                    val methodId = method.methodId
                    val methodTableEntry = MethodTableEntry(
                        methodId.name,
                        methodId.protoId
                    )
                    _virtualTable[methodTableEntry] = methodTableEntry
                }
                for (method in it.directMethods) {
                    val methodId = method.methodId
                    val methodTableEntry = MethodTableEntry(
                        methodId.name,
                        methodId.protoId
                    )
                    _virtualTable[methodTableEntry] = methodTableEntry
                }
            }
            // update the i-table with the current class's interfaces
            for (interfaceTypeId in interfaces) {
                for (method in interfaceTypeId.virtualTable) {
                    val methodID = method.value
                    // check if my v-table has the same method
                    if (_virtualTable.containsKey(methodID)) {
                        _interfaceTable[method.key] = methodID
                    } else {
                        throw IllegalStateException(
                            "Required interface method ${method.key} not implemented in" +
                                " ${classDef.classDef.typeId.descriptor}"
                        )
                    }
                }
            }
        }
    }

    class MockedReference(
        val clazz: Class<*>,
        override val directSuperClass: Type?,
        override val interfaces: List<Type>
    ) : Type() {
        override val descriptor: String = clazz.descriptorString()
        override val interfaceTable: Map<MethodTableEntry, MethodTableEntry>
            get() = _interfaceTable
        private val _interfaceTable: MutableMap<MethodTableEntry, MethodTableEntry> = mutableMapOf()
        override val virtualTable: Map<MethodTableEntry, MethodTableEntry>
            get() = _virtualTable
        private val _virtualTable: MutableMap<MethodTableEntry, MethodTableEntry> = mutableMapOf()

        init {
            // populate v-table and i-table of the class
            // First copy the super class's v-table and i-table
            if (directSuperClass != null) {
                _interfaceTable.putAll(directSuperClass.interfaceTable)
                _virtualTable.putAll(directSuperClass.virtualTable)
            }
            // update the v-table with the current class's methods
            for (method in clazz.methods) {
                val methodId = method.methodId()
                val methodTableEntry = MethodTableEntry(
                    methodId.name,
                    methodId.protoId
                )
                _virtualTable[methodTableEntry] = methodTableEntry
            }
            // update the i-table with the current class's interfaces
            for (interfaceTypeId in interfaces) {
                for (method in interfaceTypeId.virtualTable) {
                    val methodID = method.value
                    // check if my v-table has the same method
                    if (_virtualTable.containsKey(methodID)) {
                        _interfaceTable[method.key] = methodID
                    } else {
                        throw IllegalStateException("Required interface method ${method.key} not implemented in ${clazz.name}")
                    }
                }
            }
        }

        override fun isAssignableTo(other: Type): Boolean {
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
}

fun Class<*>.shortyDescriptor(): String {
    return when (this) {
        java.lang.Boolean.TYPE -> "Z"
        java.lang.Byte.TYPE -> "B"
        java.lang.Short.TYPE -> "S"
        java.lang.Character.TYPE -> "C"
        java.lang.Integer.TYPE -> "I"
        java.lang.Long.TYPE -> "J"
        java.lang.Float.TYPE -> "F"
        java.lang.Double.TYPE -> "D"
        java.lang.Void.TYPE -> "V"
        else -> "L"
    }
}

fun Method.shortyDescriptor(): String {
    val sb = StringBuilder()
    sb.append(returnType.shortyDescriptor())
//    sb.append("(")
    sb.append(parameterTypes.joinToString("") {
        it.shortyDescriptor()
    })
//    sb.append(")")
    return sb.toString()
}

fun Method.methodId(): MethodId {
    return MethodId(
        TypeId(this.declaringClass.descriptorString()),
        ProtoId(
            ShortyDescriptor(this.shortyDescriptor()),
            TypeId(this.returnType.descriptorString()),
            0, // Dummy
        ).apply {
            parameters = this@methodId.parameterTypes.map {
                TypeId(it.descriptorString())
            }
        },
        this.name
    )
}

fun Method.methodTableEntry(): MethodTableEntry {
    return MethodTableEntry(
        this.name,
        ProtoId(
            ShortyDescriptor(this.shortyDescriptor()),
            TypeId(this.returnType.descriptorString()),
            0, // Dummy
        ).apply {
            parameters = this@methodTableEntry.parameterTypes.map {
                TypeId(it.descriptorString())
            }
        }
    )
}