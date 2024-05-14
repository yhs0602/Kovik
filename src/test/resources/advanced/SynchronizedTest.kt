package com.example.sample

object SynchronizedTest {
    private var counter = 0

    @Synchronized
    fun increment() {
        counter += 1
    }

    fun showCounter() {
        println("Counter: $counter")
    }

    fun doTest() {
        val threads = List(10) {
            Thread {
                increment()
            }
        }
        threads.forEach(Thread::start)
        threads.forEach { it.join() }
        showCounter()
    }
}