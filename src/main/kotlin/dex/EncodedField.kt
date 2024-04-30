package com.yhs0602.dex

data class EncodedField(
    val fieldIdxDiff: Int,
    val accessFlags: AccessFlags,
) {
    lateinit var fieldId: FieldId
}