package com.yhs0602.dex

data class ProtoId(
    val shorty: String,
    val returnType: TypeId,
    val parametersOff: Int
)