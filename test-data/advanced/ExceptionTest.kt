package com.example.sample

object ExceptionTest {
    fun doTest() {
        try {
            val result = 5 / 0
            println("Result: $result")
        } catch (e: ArithmeticException) {
            println("Caught an ArithmeticException: ${e.message}")
        }
        try {
            throw NullPointerException("This is a custom exception")
        } catch (e: NullPointerException) {
            println("Caught a NullPointerException: ${e.message}")
        } finally {
            println("This is the finally block")
        }
    }
}
