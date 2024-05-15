package com.yhs0602.vm.classloader

import com.yhs0602.vm.MethodWrapper

class ArrayType(
    val elementType: Type,
    clonableType: Type,
    serializableType: Type
) : Type() {
    override val directSuperClass: Type = ObjectType // 모든 배열 타입의 슈퍼 클래스는 Object
    override val interfaces: List<Type> = listOf(clonableType, serializableType) // 배열이 구현하는 인터페이스
    override val interfaceTable: Map<MethodTableEntry, MethodTableEntry> = emptyMap()
    override val virtualTable: Map<MethodTableEntry, MethodTableEntry> = emptyMap()
    override val descriptor: String = "[" + elementType.descriptor
    override val methods: Map<MethodTableEntry, MethodWrapper> = emptyMap()
    override val staticMethods: Map<MethodTableEntry, MethodWrapper> = emptyMap()
    override val constructors: Map<MethodTableEntry, MethodWrapper> = emptyMap()
    override val clazz: Class<*> = java.lang.reflect.Array.newInstance(elementType.clazz, 0).javaClass
    override fun callClInit() {} // 배열 타입은 clinit이 없음
}
