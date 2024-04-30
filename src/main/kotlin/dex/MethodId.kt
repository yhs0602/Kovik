package com.yhs0602.dex

data class MethodId(val typeId: TypeId, val protoId: ProtoId, val name: String)
// membername = Simplename or <Simplename>