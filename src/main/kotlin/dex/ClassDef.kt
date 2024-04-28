package com.yhs0602.dex

data class ClassDef(
    val typeId: TypeId,
    val accessFlags: Int,
    val superClassTypeId: TypeId?,
    val interfacesOff: Int,
    val sourceFile: String?,
    val annotationsOff: Int,
    val classDataOff: Int,
    val staticValuesOff: Int
)
