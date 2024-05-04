package com.example.sample

class WideTest {
    fun testLong() {
        val longValue = 100L
        val longValue2 = 200L
        println("$longValue + $longValue2 = ${longValue + longValue2}")
        val floatValue = 100.0f
        val doubleValue = floatValue.toDouble()
        println("Float: $floatValue, Double: $doubleValue")
    }
}
