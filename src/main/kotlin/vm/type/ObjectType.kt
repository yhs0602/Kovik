package com.yhs0602.vm.type

import com.yhs0602.vm.MethodWrapper
import com.yhs0602.vm.classloader.MethodTableEntry
import com.yhs0602.vm.classloader.methodTableEntry
import com.yhs0602.vm.instance.Instance
import com.yhs0602.vm.instance.MockedInstance
import com.yhs0602.vm.makeMockedConstructor
import com.yhs0602.vm.makeMockedMethod
import java.lang.reflect.Modifier

data object ObjectType : Type() {
    override fun isAssignableTo(other: Type): Boolean {
        return true
    }

    override val directSuperClass: Type? = null
    override val interfaces: List<Type> = emptyList()
    override val interfaceTable: Map<MethodTableEntry, MethodTableEntry>
        get() = _interfaceTable
    private val _interfaceTable: MutableMap<MethodTableEntry, MethodTableEntry> = mutableMapOf()
    override val virtualTable: Map<MethodTableEntry, MethodTableEntry>
        get() = _virtualTable
    private val _virtualTable: MutableMap<MethodTableEntry, MethodTableEntry> = mutableMapOf()
    override val descriptor: String = "Ljava/lang/Object;"
    override val methods = mutableMapOf<MethodTableEntry, MethodWrapper>()
    override val staticMethods = mutableMapOf<MethodTableEntry, MethodWrapper>()
    override val constructors = mutableMapOf<MethodTableEntry, MethodWrapper>()
    override val clazz: Class<*> = java.lang.Object::class.java
    override fun callClInit() {} // no clinit for Object

    init {
        // populate v-table and i-table of Object
        for (method in java.lang.Object::class.java.methods) {
            val methodId = method.methodTableEntry()
            if (Modifier.isStatic(method.modifiers)) {
                staticMethods[methodId] = MethodWrapper.Mocked(
                    makeMockedMethod(java.lang.Object::class.java, method)
                )
            } else {
                _virtualTable[methodId] = methodId
                methods[methodId] = MethodWrapper.Mocked(
                    makeMockedMethod(java.lang.Object::class.java, method)
                )
            }
        }
        for (constructorMethod in java.lang.Object::class.java.constructors) {
            val methodId = constructorMethod.methodTableEntry()
            constructors[methodId] = MethodWrapper.Mocked(
                makeMockedConstructor(java.lang.Object::class.java, constructorMethod)
            )
        }
        // no i table for Object
    }

    override fun createInstance(): Instance {
        return MockedInstance(Object::class.java)
    }
}