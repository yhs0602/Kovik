package com.yhs0602

import com.yhs0602.dex.DexFile
import com.yhs0602.vm.Environment
import com.yhs0602.vm.GeneralMockedClass
import com.yhs0602.vm.RegisterValue
import com.yhs0602.vm.executeMethod
import java.io.File
import java.io.PrintStream
import java.io.Serializable
import java.util.*
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater
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
    val packageName = "com.example.sample"// readlnOrNull() ?: return
    val packageNameStr = packageName.replace(".", "/")
    val classes = parsedDexes.flatMap { it.classDefs }
    val className = "CoroutineExample"
    // TargetMethods StaticExample WideTest CallStatic testObjects ObjectExampleKt
    val classNameStr = "L$packageNameStr/$className;"
    val classDef = classes.find { it.classDef.typeId.descriptor == classNameStr } ?: return
    val methods = classDef.classData?.run {
        directMethods + virtualMethods
    } ?: run {
        println("No methods found")
        return
    }
    val methodName = "doTest" // readlnOrNull() ?: return
    val method = methods.find { it.methodId.name == methodName } ?: return
    val codeItem = method.codeItem ?: run {
        println("No code found")
        return
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
        GeneralMockedClass(Thread::class.java),
        GeneralMockedClass(Runnable::class.java),
        GeneralMockedClass(Serializable::class.java),
        GeneralMockedClass(ThreadLocal::class.java),
        GeneralMockedClass(java.lang.Boolean::class.java),
        GeneralMockedClass(AtomicReferenceFieldUpdater::class.java),
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
            println("$indentation Before $instruction: $pc// ${memory.registers.toList()} exception=${memory.exception}, returnValue = ${memory.returnValue.contentToString()}") // Debug
        },
        afterInstruction = { pc, instruction, memory, depth ->
            val indentation = "    ".repeat(depth)
            println("$indentation After $instruction: $pc// ${memory.registers.toList()} exception=${memory.exception}, returnValue = ${memory.returnValue.contentToString()}") // Debug
        }
    )
    executeMethod(codeItem, environment, args, codeItem.insSize.toInt(), 0)
}
