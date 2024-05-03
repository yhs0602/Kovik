package com.yhs0602

import com.yhs0602.dex.DexFile
import com.yhs0602.mockedclass.MockedStringBuilder
import com.yhs0602.mockedmethod.StringBuilderAppend
import com.yhs0602.mockedmethod.StringBuilderAppendI
import com.yhs0602.mockedmethod.StringBuilderInit
import com.yhs0602.vm.Environment
import com.yhs0602.vm.RegisterValue
import com.yhs0602.vm.executeMethod
import java.io.File

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
    for (dex in parsedDexes) {
        println(dex.header)
        println(dex.typeIds)
        println(dex.protoIds)
        println(dex.fieldIds)
        println(dex.methodIds)
        println(dex.classDefs)
        println(dex.callSiteIds)
        println(dex.methodHandles)
//        println(dex.strings)
        break
    }
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
    val className = "TargetMethods" // readlnOrNull() ?: return
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
    println(codeItem)
    val code = codeItem.insns
    for (insn in code) {
        println(insn)
    }
    // Input parameters based on the method signature
    val args = Array<RegisterValue>(codeItem.insSize.toInt()) {
        RegisterValue.Int(0)
    }
    val mockedMethodList = listOf(
        StringBuilderInit(),
        StringBuilderAppend(),
        StringBuilderAppendI()
    )
    val mockedClassesList = listOf(
        MockedStringBuilder()
    )
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
    ) { instruction, memory ->
        println("After $instruction: ${memory.registers.toList()} exception=${memory.exception}") // Debug
    }
    executeMethod(codeItem, environment, args, codeItem.insSize.toInt())
}