# Kovik

Dalvik emulator written in Kotlin, highly inspired by [Katalina](https://github.com/huuck/Katalina)

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
    val className = "StaticExample" // readlnOrNull() ?: return TargetMethods StaticExample
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
    val methodName = "doTest" // readlnOrNull() ?: return
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
        beforeInstruction = { pc, instruction, memory ->
            println("Before $instruction: $pc// ${memory.registers.toList()} exception=${memory.exception}") // Debug
        },
        afterInstruction = { pc, instruction, memory ->
            println("After $instruction: $pc// ${memory.registers.toList()} exception=${memory.exception}") // Debug
        }
    )
    executeMethod(codeItem, environment, args, codeItem.insSize.toInt())
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

## TODO
- Mock non-intrinsic Objects better
- JNI callback
- Optimization

# Open Source
- Read some code of [Katalina](https://github.com/huuck/Katalina)
- Read some code of [Android Open Source Project](https://android.googlesource.com/platform/dalvik.git/+/android-4.2.2_r1) after commit abba8ab

# CGLib
use ```
--add-opens java.base/java.lang=ALL-UNNAMED
```
options to run CGLib