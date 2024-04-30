package com.yhs0602.vm

class Frame(
    val registerSize: Int,
) {
    val registers = Array<RegisterValue>(registerSize) { RegisterValue() }

    companion object {
        fun empty(): Frame {
            return Frame(0)
        }
    }
}
