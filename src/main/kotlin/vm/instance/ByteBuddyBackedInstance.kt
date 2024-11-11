package com.yhs0602.vm.instance

import com.yhs0602.vm.Environment
import com.yhs0602.vm.RegisterValue
import com.yhs0602.vm.type.Type

// TODO: Backing instance
class ByteBuddyBackedInstance(val type: Type) : Instance() {
    lateinit var backingValue: Any
    override fun getField(idx: Int): Array<RegisterValue>? {
        TODO()
    }

    override fun setField(idx: Int, value: Array<RegisterValue>) {
        // set field using reflection
        val field = type.clazz.fields[idx]
        val marshaledValue = marshalArgument(
            environment = Environment.getInstance(),
            args = value.toList(),
            idx = 0,
            paramType = field.type
        )
        field.set(backingValue, marshaledValue.first)
    }

    override fun toString(): String {
        if (this::backingValue.isInitialized) {
            return "ByteBuddyBackedInstance(type=$type, value=$backingValue)"
        }
        return "ByteBuddyBackedInstance(type=$type, value=Uninitialized)"
    }
}