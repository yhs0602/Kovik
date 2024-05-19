package com.yhs0602.vm.classloader

import com.yhs0602.dex.MethodId
import com.yhs0602.dex.ProtoId
import com.yhs0602.dex.ShortyDescriptor
import com.yhs0602.dex.TypeId
import java.lang.reflect.Constructor
import java.lang.reflect.Method

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
        },
        method = this
    )
}

fun Constructor<*>.shortyDescriptor(): String {
    val sb = StringBuilder()
    sb.append("V")
    sb.append(parameterTypes.joinToString("") {
        it.shortyDescriptor()
    })
    return sb.toString()
}

fun Constructor<*>.methodId(): MethodId {
    return MethodId(
        TypeId(this.declaringClass.descriptorString()),
        ProtoId(
            ShortyDescriptor(this.shortyDescriptor()),
            TypeId("V"),
            0, // Dummy
        ).apply {
            parameters = this@methodId.parameterTypes.map {
                TypeId(it.descriptorString())
            }
        },
        "<init>"
    )
}

fun Constructor<*>.methodTableEntry(): MethodTableEntry {
    return MethodTableEntry(
        "<init>",
        ProtoId(
            ShortyDescriptor(this.shortyDescriptor()),
            TypeId("V"),
            0, // Dummy
        ).apply {
            parameters = this@methodTableEntry.parameterTypes.map {
                TypeId(it.descriptorString())
            }
        },
        null
    )
}
