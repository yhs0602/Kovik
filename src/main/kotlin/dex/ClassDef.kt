package com.yhs0602.dex

data class ClassDef(
    val typeId: TypeId,
    val accessFlags: AccessFlags,
    val superClassTypeId: TypeId?,
    val interfacesOff: Int,
    val sourceFile: String?,
    val annotationsOff: Int,
    val classDataOff: Int,
    val staticValuesOff: Int
) {
    lateinit var flattendInterfaces: List<TypeId>
    lateinit var interfaces: List<TypeId>
}
