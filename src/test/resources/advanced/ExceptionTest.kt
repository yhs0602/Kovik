package com.example.sample

object ExceptionTest {
    fun doTest() {
        var a = 1
        try {
            val result = 5 / 0
            a = a or 2
            println("Result: $result")
        } catch (e: ArithmeticException) {
            println("Caught an ArithmeticException: ${e.message}")
            a = a or 4
        }
        try {
            throw NullPointerException("This is a custom exception")
            a = a or 8
        } catch (e: NullPointerException) {
            println("Caught a NullPointerException: ${e.message}")
            a = a or 16
        } finally {
            a = a or 32
            println("This is the finally block")
        }
        assert(a == 53)
    }
}