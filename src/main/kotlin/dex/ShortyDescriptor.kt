package com.yhs0602.dex

data class ShortyDescriptor(val shorty: String) {
    val returnType: String = shorty.substring(0, 1)
    val parameterTypes: List<String> = shorty.substring(1).map { it.toString() }
    val parameterCount: Int = parameterTypes.size
    val humanReadable: String by lazy {
        buildString {
            append(names[returnType] ?: returnType)
            append("(")
            append(parameterTypes.joinToString(",") { names[it] ?: it })
            append(")")
        }
    }

    override fun toString(): String {
        return humanReadable
    }

    companion object {
        val names = mapOf(
            "V" to "void",
            "Z" to "boolean",
            "B" to "byte",
            "S" to "short",
            "C" to "char",
            "I" to "int",
            "J" to "long",
            "F" to "float",
            "D" to "double",
            "L" to "object",
        )
    }
}