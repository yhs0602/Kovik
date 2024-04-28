package com.yhs0602.dex

import java.io.RandomAccessFile

data class Header(
    val magic: ByteArray,
    val checksum: Int,
    val signature: ByteArray,
    val fileSize: Int,
    val headerSize: Int,
    val endianTag: Int,
    val linkSize: Int,
    val linkOff: Int,
    val mapOff: Int,
    val stringIdsSize: Int,
    val stringIdsOff: Int,
    val typeIdsSize: Int,
    val typeIdsOff: Int,
    val protoIdsSize: Int,
    val protoIdsOff: Int,
    val fieldIdsSize: Int,
    val fieldIdsOff: Int,
    val methodIdsSize: Int,
    val methodIdsOff: Int,
    val classDefsSize: Int,
    val classDefsOff: Int,
    val dataSize: Int,
    val dataOff: Int
) {
    companion object {
        fun fromDataInputStream(randomAccessFile: RandomAccessFile): Header {
            val magic = ByteArray(8)
            randomAccessFile.read(magic)
            val checksum = randomAccessFile.readInt()
            val signature = ByteArray(20)
            randomAccessFile.read(signature)
            val fileSize = randomAccessFile.readInt()
            val headerSize = randomAccessFile.readInt()
            val endianTag = randomAccessFile.readInt()
            val linkSize = randomAccessFile.readInt()
            val linkOff = randomAccessFile.readInt()
            val mapOff = randomAccessFile.readInt()
            val stringIdsSize = randomAccessFile.readInt()
            val stringIdsOff = randomAccessFile.readInt()
            val typeIdsSize = randomAccessFile.readInt()
            val typeIdsOff = randomAccessFile.readInt()
            val protoIdsSize = randomAccessFile.readInt()
            val protoIdsOff = randomAccessFile.readInt()
            val fieldIdsSize = randomAccessFile.readInt()
            val fieldIdsOff = randomAccessFile.readInt()
            val methodIdsSize = randomAccessFile.readInt()
            val methodIdsOff = randomAccessFile.readInt()
            val classDefsSize = randomAccessFile.readInt()
            val classDefsOff = randomAccessFile.readInt()
            val dataSize = randomAccessFile.readInt()
            val dataOff = randomAccessFile.readInt()

            return Header(
                magic,
                checksum,
                signature,
                fileSize,
                headerSize,
                endianTag,
                linkSize,
                linkOff,
                mapOff,
                stringIdsSize,
                stringIdsOff,
                typeIdsSize,
                typeIdsOff,
                protoIdsSize,
                protoIdsOff,
                fieldIdsSize,
                fieldIdsOff,
                methodIdsSize,
                methodIdsOff,
                classDefsSize,
                classDefsOff,
                dataSize,
                dataOff
            )
        }
    }
}