# Kovik

Dalvik emulator written in Kotlin, highly inspired by [Katalina](https://github.com/huuck/Katalina)

## Ability
It can run the code below seamlessly.
```kotlin
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
```

## Usage

This example shows how to parse a .dex file and print out some information about it.

```kotlin
package com.yhs0602

import com.yhs0602.dex.DexFile
import com.yhs0602.vm.Environment
import com.yhs0602.vm.GeneralMockedClass
import com.yhs0602.vm.RegisterValue
import com.yhs0602.vm.executeMethod
import java.io.File
import java.io.PrintStream
import java.util.Arrays
import kotlin.jvm.internal.Intrinsics


fun main() {
    // Surprisingly, multidex is default nowadays
    println("Enter the path of the folder of dexes:")
    val path = readlnOrNull() ?: return
    val dexes = File(path).listFiles { _, name ->
        name.endsWith(".dex")
    } ?: return
    dexes.forEach {
        println(it.name)
    }
    val parsedDexes = dexes.map {
        DexFile.fromFile(it)
    }
//    for (dex in parsedDexes) {
//        println(dex.header)
//        println(dex.typeIds)
//        println(dex.protoIds)
//        println(dex.fieldIds)
//        println(dex.methodIds)
//        println(dex.classDefs)
//        println(dex.callSiteIds)
//        println(dex.methodHandles)
////        println(dex.strings)
//        break
//    }
    // Find entry point, and setup start environment
    println("Enter the package name to search:")
    val packageName = "com.example.sample"// readlnOrNull() ?: return
    val packageNameStr = packageName.replace(".", "/")
    println("Classes====================")
    val classes = parsedDexes.flatMap { it.classDefs }
    for (clazz in classes) {
        if (clazz.classDef.typeId.descriptor.startsWith("L$packageNameStr/")) {
            println(clazz)
        }
    }
    println("Enter the class name you are interested in:")
    val className =
        "ObjectExampleKt" // readlnOrNull() ?: return TargetMethods StaticExample WideTest CallStatic testObjects
    val classNameStr = "L$packageNameStr/$className;"
    val classDef = classes.find { it.classDef.typeId.descriptor == classNameStr } ?: return
    println("Methods====================")
    val methods = classDef.classData?.run {
        directMethods + virtualMethods
    } ?: run {
        println("No methods found")
        return
    }
    for (method in methods) {
        println(method)
    }
    println("Enter the method name you are interested in:")
    val methodName = "testObjects" // readlnOrNull() ?: return
    val method = methods.find { it.methodId.name == methodName } ?: return
    println("Code====================")
    val codeItem = method.codeItem ?: run {
        println("No code found")
        return
    }
//    println(codeItem)
    val code = codeItem.insns
    for (insn in code) {
        println(insn)
    }
    // Input parameters based on the method signature
    val args = Array<RegisterValue>(codeItem.insSize.toInt()) {
        RegisterValue.Int(0)
    }
    val mockedClassesList = listOf(
        GeneralMockedClass(StringBuilder::class.java),
        GeneralMockedClass(PrintStream::class.java),
        GeneralMockedClass(System::class.java),
        GeneralMockedClass(Intrinsics::class.java),
        GeneralMockedClass(Object::class.java),
        GeneralMockedClass(Math::class.java),
        GeneralMockedClass(File::class.java),
        GeneralMockedClass(Arrays::class.java),
        GeneralMockedClass(Comparable::class.java),
    )
    val mockedMethodList = mockedClassesList.flatMap {
        it.getMethods()
    }

    val mockedMethods = mockedMethodList.associateBy {
        Triple(it.classId, it.parameters, it.name)
    }
    val mockedClasses = mockedClassesList.associateBy {
        it.classId
    }
    val environment = Environment(
        parsedDexes,
        mockedMethods,
        mockedClasses,
        beforeInstruction = { pc, instruction, memory, depth ->
            val indentation = "    ".repeat(depth)
            println("$indentation Before $instruction: $pc// ${memory.registers.toList()} exception=${memory.exception}") // Debug
        },
        afterInstruction = { pc, instruction, memory, depth ->
            val indentation = "    ".repeat(depth)
            println("$indentation After $instruction: $pc// ${memory.registers.toList()} exception=${memory.exception}") // Debug
        }
    )
    executeMethod(codeItem, environment, args, codeItem.insSize.toInt(), 0)
}
```

## Current status

- Basic .dex parsing support
- Parse method body
- Emulate instructions
- Represent real / mocked (lazy) values
- Mock System static fields
- Easier mocking of classes
- Call <clinit> for initialization of static fields
- Mock non-intrinsic Objects

## Test pass status
- [x] minimal
- [x] callstatic
- [x] object
- [x] singleton
- [x] static
- [x] wide
- [x] ThreadExample
- [ ] CoroutineExample
- [ ] AnnotationTest
- [ ] ExceptionTest
- [ ] IOTest
- [ ] NativeExample
- [ ] ReflectionTest
- [ ] SynchronizedTest

## TODO
- Handle ClassRef and reflection using ByteBuddy
- JNI callback
- Optimization

# Open Source
- Read some code of [Katalina](https://github.com/huuck/Katalina)
- Read some code of [Android Open Source Project](https://android.googlesource.com/platform/dalvik.git/+/android-4.2.2_r1) after commit abba8ab

# CGLib
use 
```
--add-opens java.base/java.lang=ALL-UNNAMED
```
JVM option if you encounter issues with CGLib.