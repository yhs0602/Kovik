package com.yhs0602.vm.instance

import com.yhs0602.vm.RegisterValue
import com.yhs0602.vm.type.Type

// TODO: Backing instance
class ByteBuddyBackedInstance(val type: Type) : Instance() {
    lateinit var value: Any
    override fun getField(idx: Int): Array<RegisterValue>? {
        TODO()
    }

    override fun setField(idx: Int, value: Array<RegisterValue>) {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        if (this::value.isInitialized) {
            return "ByteBuddyBackedInstance(type=$type, value=$value)"
        }
        return "ByteBuddyBackedInstance(type=$type, value=Uninitialized)"
    }
}