package com.example.sample

object ReflectionTest {
    fun doTest() {
        val clazz = String::class.java
        println("Class name: ${clazz.name}")

        clazz.methods.take(5).forEach {
            println("Method name: ${it.name}")
        }
    }
}
