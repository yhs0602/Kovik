package com.yhs0602.vm.instance

import com.yhs0602.vm.RegisterValue
import com.yhs0602.vm.type.Type

// TODO: Backing instance
class ByteBuddyBackedInstance(val type: Type) : Instance() {
    override fun getField(idx: Int): Array<RegisterValue>? {
        TODO()
    }

    override fun setField(idx: Int, value: Array<RegisterValue>) {
        TODO("Not yet implemented")
    }
}