package com.example.sample

import java.io.*

object IOTest {
    fun doTest() {
        val fileName = "test.txt"
        try {
            PrintWriter(fileName).use { out -> out.println("Hello, world!") }
            BufferedReader(FileReader(fileName)).use { br ->
                println("File content: ${br.readLine()}")
            }
        } catch (e: IOException) {
            println("An error occurred: ${e.message}")
        }
    }
}
