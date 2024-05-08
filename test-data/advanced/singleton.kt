package com.example.sample

object SingletonExample {
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

    fun doTest() {
        printMessage()
        printMessage("Hello")
        incrementCount()
        incrementCount()
        println("Count: $count")
    }
}
