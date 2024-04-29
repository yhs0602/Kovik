package com.yhs0602

import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder

class EndianAwareRandomAccessFile(
    file: File,
    mode: String,
    private var littleEndian: Boolean = true
) {
    private val raf = RandomAccessFile(file, mode)
    private fun readBytes(size: Int): ByteArray {
        val data = ByteArray(size)
        raf.readFully(data)
        return data
    }

    fun setLittleEndian(le: Boolean) {
        littleEndian = le
    }

    fun readInt(): Int {
        val b1 = raf.read()
        val b2 = raf.read()
        val b3 = raf.read()
        val b4 = raf.read()
        return if (littleEndian) {
            (b4 shl 24) or (b3 shl 16) or (b2 shl 8) or b1
        } else {
            (b1 shl 24) or (b2 shl 16) or (b3 shl 8) or b4
        }
    }

    fun readShort(): Short {
        val b1 = raf.read()
        val b2 = raf.read()
        return if (littleEndian) {
            ((b2 shl 8) or b1).toShort()
        } else {
            ((b1 shl 8) or b2).toShort()
        }
    }

    fun readLong(): Long {
        val i1 = readInt()
        val i2 = readInt()
        return if (littleEndian) {
            (i2.toLong() shl 32) or (i1.toLong() and 0xFFFFFFFFL)
        } else {
            (i1.toLong() shl 32) or (i2.toLong() and 0xFFFFFFFFL)
        }
    }

    fun readUnsignedByte(): Int {
        return raf.readUnsignedByte()
    }

    fun readByte(): Byte {
        return raf.readByte()
    }

    fun readChar() = raf.readChar()


    fun read() = raf.read()

    fun read(bytes: ByteArray) {
        raf.read(bytes)
    }

    fun readFloat(): Float {
        val data = readBytes(4)
        val buffer = ByteBuffer.wrap(data)
        buffer.order(if (littleEndian) ByteOrder.LITTLE_ENDIAN else ByteOrder.BIG_ENDIAN)
        return buffer.float
    }

    fun readDouble(): Double {
        val data = readBytes(8)
        val buffer = ByteBuffer.wrap(data)
        buffer.order(if (littleEndian) ByteOrder.LITTLE_ENDIAN else ByteOrder.BIG_ENDIAN)
        return buffer.double
    }

    fun readUnsignedShort(): Int {
        val b1 = raf.read()
        val b2 = raf.read()
        return if (littleEndian) {
            (b2 shl 8) or b1
        } else {
            (b1 shl 8) or b2
        }
    }



    fun readFully(b: ByteArray, off: Int, len: Int) {
        raf.readFully(b, off, len)
    }

    fun seek(pos: Long) {
        raf.seek(pos)
    }

    fun close() {
        raf.close()
    }
}