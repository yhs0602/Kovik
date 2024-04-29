package com.yhs0602

import com.yhs0602.dex.DexFile
import java.io.File
import java.io.RandomAccessFile

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
        println(dex)
    }
}