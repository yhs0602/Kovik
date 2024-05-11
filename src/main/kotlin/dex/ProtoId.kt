package com.yhs0602.dex

data class ProtoId(
    val shorty: ShortyDescriptor,
    val returnType: TypeId,
    val parametersOff: Int
) {
    lateinit var parameters: List<TypeId>
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProtoId

        if (shorty != other.shorty) return false
        if (returnType != other.returnType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = shorty.hashCode()
        result = 31 * result + returnType.hashCode()
        return result
    }

}