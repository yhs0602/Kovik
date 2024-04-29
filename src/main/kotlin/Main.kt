package com.yhs0602

import com.yhs0602.dex.DexFile
import com.yhs0602.dex.MethodId
import java.io.File

fun main() {
    // Surprisingly, multidex is default nowadays
    println("Enter the path of the folder of dexes:")
    val path = readlnOrNull() ?: return
    val dexes = File(path).listFiles() ?: return
    dexes.forEach {
        println(it.name)
    }
    val parsedDexes = dexes.map {
        DexFile.fromFile(EndianAwareRandomAccessFile(it, "r"))
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
    val packageName = readlnOrNull() ?: return
    val packageNameStr = packageName.replace(".", "/")
    println("Classes====================")
    val classes = parsedDexes.flatMap { it.classDefs }
    for (clazz in classes) {
        if (clazz.typeId.descriptor.startsWith("L$packageNameStr/")) {
            println(clazz)
        }
    }
    println("Enter the class name you are interested in:")
    val className = readlnOrNull() ?: return
    val classNameStr = "L$packageNameStr/$className;"
    val classDef = classes.find { it.typeId.descriptor == classNameStr } ?: return
    println("Methods====================")
    val methods = parsedDexes.flatMap { it.methodIds }
    val interestingMethods = mutableListOf<MethodId>()
    for (method in methods) {
        if (method.typeId.descriptor.startsWith(classNameStr)) {
            println(method)
            interestingMethods.add(method)
        }
    }
    println("Enter the method name you are interested in:")
    val methodName = readlnOrNull() ?: return
    val interstingMethod = interestingMethods.find { it.s == methodName } ?: return
    // TODO: Find the code item
}