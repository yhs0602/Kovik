package com.example.sample

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

object CoroutineExample {
    suspend fun simulateLongRunningTask(): Int {
        delay(1000) // Simulate a long-running task
        return 42 // Return some computed result
    }

    fun doTest() = runBlocking {
        val result = async { simulateLongRunningTask() }
        println("Coroutine result: ${result.await()}")
    }
}
