package com.example.sample

class ThreadExample {
    fun doTest() {
        val thread = Thread(RunnableExample())
        thread.start()
        thread.join()
    }
}

class RunnableExample : Runnable {
    override fun run() {
        println("Running in a separate thread...")
    }
}
