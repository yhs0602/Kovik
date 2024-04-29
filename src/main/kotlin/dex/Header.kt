package com.yhs0602.dex

import com.yhs0602.EndianAwareRandomAccessFile


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
        const val ENDIAN_CONSTANT = 0x12345678
        const val REVERSE_ENDIAN_CONSTANT = 0x78563412

        fun fromDataInputStream(randomAccessFile: EndianAwareRandomAccessFile): Header {
            val magic = ByteArray(8)
            randomAccessFile.read(magic)
            val checksum = randomAccessFile.readInt()
            val signature = ByteArray(20)
            randomAccessFile.read(signature)
            var fileSize = randomAccessFile.readInt()
            var headerSize = randomAccessFile.readInt()
            val endianTag = randomAccessFile.readInt()
            if (endianTag == ENDIAN_CONSTANT) {
                randomAccessFile.setLittleEndian(true)
            } else if (endianTag == REVERSE_ENDIAN_CONSTANT) {
                randomAccessFile.setLittleEndian(false)
            }
            randomAccessFile.seek(32)
            fileSize = randomAccessFile.readInt()
            headerSize = randomAccessFile.readInt()
            randomAccessFile.readInt() // to skip endianTag
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