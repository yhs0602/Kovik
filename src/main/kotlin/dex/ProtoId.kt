package com.yhs0602.dex

data class ProtoId(
    val shorty: ShortyDescriptor,
    val returnType: TypeId,
    val parametersOff: Int
) {
    lateinit var parameters: List<TypeId>
}