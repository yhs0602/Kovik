package com.yhs0602.vm.classloader

import com.yhs0602.vm.MethodWrapper

class MockedType(
    override val clazz: Class<*>,
    override val directSuperClass: Type?,
    override val interfaces: List<Type>
) : Type() {
    override val descriptor: String = clazz.descriptorString()
    override val interfaceTable: Map<MethodTableEntry, MethodTableEntry>
        get() = _interfaceTable
    private val _interfaceTable: MutableMap<MethodTableEntry, MethodTableEntry> = mutableMapOf()
    override val virtualTable: Map<MethodTableEntry, MethodTableEntry>
        get() = _virtualTable
    private val _virtualTable: MutableMap<MethodTableEntry, MethodTableEntry> = mutableMapOf()
    override val methods = mutableMapOf<MethodTableEntry, MethodWrapper>()
    override val constructors = mutableMapOf<MethodTableEntry, MethodWrapper>()

    init {
        // populate v-table and i-table of the class
        // First copy the super class's v-table and i-table
        if (directSuperClass != null) {
            _interfaceTable.putAll(directSuperClass.interfaceTable)
            _virtualTable.putAll(directSuperClass.virtualTable)
        }
        // update the v-table with the current class's methods
        for (method in clazz.methods) {
            val methodId = method.methodId()
            val methodTableEntry = MethodTableEntry(
                methodId.name,
                methodId.protoId
            )
            _virtualTable[methodTableEntry] = methodTableEntry
        }
        // update the i-table with the current class's interfaces
        for (interfaceTypeId in interfaces) {
            for (method in interfaceTypeId.virtualTable) {
                val methodID = method.value
                // check if my v-table has the same method
                if (_virtualTable.containsKey(methodID)) {
                    _interfaceTable[method.key] = methodID
                } else {
                    throw IllegalStateException("Required interface method ${method.key} not implemented in ${clazz.name}")
                }
            }
        }
    }
}