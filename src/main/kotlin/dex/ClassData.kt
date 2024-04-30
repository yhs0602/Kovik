package com.yhs0602.dex

data class ClassData(
    val staticFields: List<EncodedField>,
    val instanceFields: List<EncodedField>,
    val directMethods: List<EncodedMethod>,
    val virtualMethods: List<EncodedMethod>
)