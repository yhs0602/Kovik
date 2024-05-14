package com.example.sample

import java.io.File
import java.util.Arrays
import kotlin.math.abs


class Point(val x: Int, val y: Int) {
    constructor(x: Int) : this(x, x)

    fun L0Norm(): Int {
        return x + y
    }

    fun L1Norm(): Int {
        return abs(x) + abs(y)
    }
}

open class AccessTest(
    val name: String,
    private val age: Int,
    protected val id: Int,
    var variable: Int = 0
) {
    fun printAge() {
        println("Age: $age")
    }

    fun printId() {
        println("Id: $id")
    }
}

class InheritanceTest(
    name: String,
    age: Int,
    id: Int,
    val address: String
) : AccessTest(name, age, id) {
    fun printAddress() {
        println("Address: $address")
    }

    fun printAll() {
        println("Name: $name")
        printAge()
        printId()
        printAddress()
    }
}

open class OverridingTest(var myProp: Int = 0) {
    open fun printMessage() {
        println("This is the parent class")
    }
}

class ChildOverridingTest : OverridingTest() {
    override fun printMessage() {
        println("This is the child class")
        myProp = 5
    }
}

open class SuperCallingTest {
    open fun printMessage() {
        println("This is the parent class")
    }
}

class ChildSuperCallingTest : SuperCallingTest() {
    override fun printMessage() {
        super.printMessage()
        println("This is the child class")
    }
}

class MockedSuperTest(
    path: String,
    val myProp: Int
) : File(path) {
    override fun exists(): Boolean {
        return true
    }

    private val myProp2 = 5
    override fun compareTo(other: File): Int {
        return myProp2
    }

    override fun toString(): String {
        val superx = super.toString()
        return "$superx, $myProp"
    }
}

class OverloadedTest {
    fun printMessage() {
        println("This is the parent class")
    }

    fun printMessage(message: String) {
        println("This is the parent class with message: $message")
    }
}

class A : Comparable<A> {
    companion object {
        fun sortSelf() {
            val As = arrayOfNulls<A>(3)
            As[0] = A()
            As[1] = A()
            As[2] = A()
            Arrays.sort(As)
        }
    }

    override fun compareTo(other: A): Int {
        return 0
    }
}


fun testObjects() {
    val point = Point(3, -4)
    println("L0 Norm: ${point.L0Norm()}") // Output: L0 Norm: -1
    println("L1 Norm: ${point.L1Norm()}") // Output: L1 Norm: 7
    val point2 = Point(5)
    println("L0 Norm: ${point2.L0Norm()}") // Output: L0 Norm: 10

    val accessTest = AccessTest("John", 25, 123)
    accessTest.printAge() // Output: Age: 25
    // accessTest.printId() // Error: Cannot access 'id': it is protected in 'AccessTest'
    val inheritanceTest = InheritanceTest("John", 25, 123, "123 Main St")
    inheritanceTest.printAll()
    // Output:
    // Name: John
    // Age: 25
    // Id: 123
    // Address: 123 Main St
    val overridingTest = OverridingTest()
    overridingTest.printMessage() // Output: This is the parent class
    val childOverridingTest = ChildOverridingTest()
    childOverridingTest.printMessage() // Output: This is the child class
    val childSuperCallingTest = ChildSuperCallingTest()
    childSuperCallingTest.printMessage()
    // Output:
    // This is the parent class
    // This is the child class
    OverloadedTest().printMessage()
    // Output: This is the parent class
    OverloadedTest().printMessage("Hello")
    // Output: This is the parent class with message: Hello

    val mockedSuperTest = MockedSuperTest("path", 10)
    println(mockedSuperTest.exists()) // Output: true
    println(mockedSuperTest.compareTo(File("path"))) // Output: 5
    println(mockedSuperTest.toString()) // Output: path, 10
    A.sortSelf()
}