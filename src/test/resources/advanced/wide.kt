package com.example.sample

fun testLong() {
    val longValue = 100L
    val longValue2 = 200L
    assert(longValue + longValue2 == 300L)
    println("$longValue + $longValue2 = ${longValue + longValue2}")
    val longMax = Long.MAX_VALUE
    val longMin = Long.MIN_VALUE
    assert(longMax + longMin == -1L)
    println("Max Long: $longMax, Min Long: $longMin")
    val largeX = 100000000000L
    val largeY = 200000000000L
    assert(largeX + largeY == 300000000000L)
    println("$largeX + $largeY = ${largeX + largeY}")
    val floatValue = 100.0f
    val doubleValue = floatValue.toDouble()
    assert(floatValue == doubleValue.toFloat())
    println("Float: $floatValue, Double: $doubleValue")
    val edgeFloat = -Float.MIN_VALUE
    val edgeDouble = edgeFloat.toDouble()
    assert(edgeFloat == edgeDouble.toFloat())
    println("Edge Float: $edgeFloat, Edge Double: $edgeDouble")
}
