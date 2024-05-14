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

        @JvmStatic
        fun doTest() {
            printMessage()
            printMessage("Hello")
            incrementCount()
            assert(count == 1)
            incrementCount()
            assert(count == 2)
            println("Count: $count")
            realStaticMethod()
            assert(count == 4)
        }
    }
}