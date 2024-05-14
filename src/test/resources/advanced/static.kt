package com.example.sample

class StaticExample {
    companion object {
        fun printMessage() {
            println("This is a static method")
        }

        fun printMessage(message: String) {
            println("This is a static method with message: $message")
        }

        var count = 0

        fun incrementCount() {
            count++
        }

        @JvmStatic
        fun realStaticMethod() {
            println("This is a real static method")
            count++
            incrementCount()
        }
    }

    fun doTest() {
        printMessage()
        printMessage("Hello")
        incrementCount()
        incrementCount()
        println("Count: $count")
        realStaticMethod()
    }
}