package com.yhs0602.dex

data class TypeId(val descriptor: String) {
    override fun toString(): String {
        return "TypeId($descriptor)"
    }
}