package com.yhs0602.vm

// Memory holds the memory of the VM
class Memory(
    val registerSize: Int
) {
    val registers = Array<RegisterValue>(registerSize) { RegisterValue.Int(0) }
    var returnValue = arrayOf<RegisterValue>()
    var exception: ExceptionValue? = null
}