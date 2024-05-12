package com.yhs0602.dex

@JvmInline
value class TypeId(val descriptor: String) {
    override fun toString(): String {
        return "TypeId($descriptor)"
    }
}