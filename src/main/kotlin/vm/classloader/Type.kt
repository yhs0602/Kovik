package com.yhs0602.vm.classloader

import com.yhs0602.dex.*
import java.lang.reflect.Method

sealed class Type {
    abstract fun instanceOf(other: Type): Boolean
    abstract val directSuperClass: Type?
    abstract val interfaceTable: Map<MethodId, MethodId>
    abstract val virtualTable: Map<MethodId, MethodId>
    abstract val descriptor: String

    data object Object : Type() {
        override fun instanceOf(other: Type): Boolean {
            return true
        }

        override val directSuperClass: Type? = null
        override val interfaceTable: Map<MethodId, MethodId>
            get() = _interfaceTable
        private val _interfaceTable: MutableMap<MethodId, MethodId> = mutableMapOf()
        override val virtualTable: Map<MethodId, MethodId>
            get() = _virtualTable
        private val _virtualTable: MutableMap<MethodId, MethodId> = mutableMapOf()
        override val descriptor: String = "Ljava/lang/Object;"

        init {
            // populate v-table and i-table of Object
            for (method in java.lang.Object::class.java.methods) {
                val methodId = method.methodId()
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

        override fun instanceOf(other: Type): Boolean {
            return this == other
        }

        override val directSuperClass: Type = Object

        // no i table for primitive types
        override val interfaceTable: Map<MethodId, MethodId> = emptyMap()

        // no v table for primitive types
        override val virtualTable: Map<MethodId, MethodId> = emptyMap()
    }

    class DexDefinedReference(
        classDef: ParsedClass,
        override val directSuperClass: Type?,
        interfaces: List<Type>
    ) : Type() {
        override fun instanceOf(other: Type): Boolean {
            // TODO: Handle inheritance
            return this == other
        }

        override val descriptor: String = classDef.classDef.typeId.descriptor
        override val interfaceTable: Map<MethodId, MethodId>
            get() = _interfaceTable
        private val _interfaceTable: MutableMap<MethodId, MethodId> = mutableMapOf()
        override val virtualTable: Map<MethodId, MethodId>
            get() = _virtualTable
        private val _virtualTable: MutableMap<MethodId, MethodId> = mutableMapOf()

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
                    _virtualTable[methodId] = methodId
                }
                for (method in it.directMethods) {
                    val methodId = method.methodId
                    _virtualTable[methodId] = methodId
                }
            }
            // update the i-table with the current class's interfaces
            for (interfaceTypeId in interfaces) {
                // TODO: Handle interfaces
            }
        }
    }

    class MockedReference(
        val clazz: Class<*>,
        override val directSuperClass: Type?,
        interfaces: List<Type>
    ) : Type() {
        override val descriptor: String = clazz.descriptorString()
        override val interfaceTable: Map<MethodId, MethodId>
            get() = _interfaceTable
        private val _interfaceTable: MutableMap<MethodId, MethodId> = mutableMapOf()
        override val virtualTable: Map<MethodId, MethodId>
            get() = _virtualTable
        private val _virtualTable: MutableMap<MethodId, MethodId> = mutableMapOf()

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
                _virtualTable[methodId] = methodId
            }
            // update the i-table with the current class's interfaces
            for (interfaceType in interfaces) {
                // TODO: Handle interfaces
            }
        }

        override fun instanceOf(other: Type): Boolean {
            // TODO
            return this == other
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