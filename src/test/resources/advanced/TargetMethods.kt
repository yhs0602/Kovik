package com.example.sample

class TargetMethods {
    fun simpleMethod(a: Int, b: Int): Int {
        return a + b
    }

    fun conditionalMethod(a: Int, b: Int): Int {
        return if (a > b) {
            a
        } else {
            b
        }
    }

    fun loopMethod(count: Int): Int {
        var sum = 0
        for (i in 1..count) {
            sum += i
        }
        return sum
    }

    fun arrayMethod(arr: IntArray): Int {
        var sum = 0
        for (num in arr) {
            sum += num
        }
        return sum
    }

    fun instanceMethod(other: TargetMethods): Int {
        return simpleMethod(3, other.conditionalMethod(5, 2))
    }


    companion object {
        @JvmStatic
        fun doTest() {
            val thisObject = TargetMethods()
            val simpleResult = thisObject.simpleMethod(2, 3)
            assert(simpleResult == 5)
            println("Simple method: $simpleResult") // Output: Simple method: 5
            val conditionalResult = thisObject.conditionalMethod(5, 3)
            assert(conditionalResult == 5)
            println("Conditional method: $conditionalResult")
            // Output: Conditional method: 5
            val loopMethodResult = thisObject.loopMethod(5)
            assert(loopMethodResult == 15)
            println("Loop method: $loopMethodResult") // Output: Loop method: 15
            val myArray = intArrayOf(1, 2, 3, 4, 5)
            val arrayMethodResult = thisObject.arrayMethod(myArray)
            assert(arrayMethodResult == 15)
            println("Array method: $arrayMethodResult") // Output: Array method: 15
            val other2Object = TargetMethods()
            val instanceMethodResult = thisObject.instanceMethod(other2Object)
            assert(instanceMethodResult == 8)
            println("Instance method: $instanceMethodResult") // Output: Instance method: 8
        }
    }
}