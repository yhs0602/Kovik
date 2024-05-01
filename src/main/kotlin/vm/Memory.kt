package com.yhs0602.vm

// Memory holds the memory of the VM
class Memory {
    var returnValue = listOf<RegisterValue>()
    var exception: ExceptionValue? = null
    var returnAddress = 0
}