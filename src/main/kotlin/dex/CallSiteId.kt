package com.yhs0602.dex

data class CallSiteId(
    val methodHandleIdx: Int,
    val methodName: String,
    val methodType: ProtoId,
    val argumentsToLinker: List<Any?>
)
