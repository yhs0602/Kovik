package com.yhs0602.dex

data class EncodedMethod(
    val methodIdxDiff: Int,
    val accessFlags: AccessFlags,
    val codeOff: Int
) {
    lateinit var methodId: MethodId
    var codeItem: CodeItem? = null

    override fun toString(): String {
        return "EncodedMethod(methodIdxDiff=$methodIdxDiff, accessFlags=$accessFlags, codeOff=$codeOff, methodId=$methodId, codeItem=$codeItem)"
    }
}