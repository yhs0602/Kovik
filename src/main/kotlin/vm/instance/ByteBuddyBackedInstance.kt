package com.yhs0602.vm.instance

import com.yhs0602.vm.RegisterValue

// TODO: Backing instance
class ByteBuddyBackedInstance(clazz: Class<*>) : Instance() {
    override fun getField(idx: Int): Array<RegisterValue>? {
        TODO()
    }

    override fun setField(idx: Int, value: Array<RegisterValue>) {
        TODO("Not yet implemented")
    }
}