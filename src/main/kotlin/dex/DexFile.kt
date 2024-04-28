package com.yhs0602.dex

import java.io.RandomAccessFile

private fun RandomAccessFile.readUnsignedLeb128(): Int {
    var result = 0
    var cur: Int
    var count = 0
    do {
        cur = read()
        result = result or (cur and 0x7f shl count * 7)
        count++
    } while (cur and 0x80 != 0)
    return result
}

private fun RandomAccessFile.readEncodedArray(): List<Any?> {
    val size = readUnsignedLeb128()
    return List(size) {
        readEncodedValue()
    }
}

private fun RandomAccessFile.readEncodedAnnotation(): Pair<Int, List<Pair<Int, Any?>>> {
    val typeIdx = readUnsignedLeb128()
    val size = readUnsignedLeb128()
    return typeIdx to List(size) {
        val nameIdx = readUnsignedLeb128()
        val value = readEncodedValue()
        nameIdx to value
    }
}

private fun RandomAccessFile.readEncodedValue(): Any? {
    val header = readUnsignedByte()
    val type = header and 0x1f
    val valueArg = header ushr 5
    return when (type) {
        0x00 -> {
            // byte
            readByte()
        }

        0x02 -> {
            // short
            readShort()
        }

        0x03 -> {
            // char
            readChar()
        }

        0x04 -> {
            // int
            readInt()
        }

        0x06 -> {
            // long
            readLong()
        }

        0x10 -> {
            // float
            readFloat()
        }

        0x11 -> {
            // double
            readDouble()
        }

        0x15 -> {
            // VALUE_METHOD_TYPE
            readInt() // unsigned int (index)
        }

        0x16 -> {
            // VALUE_METHOD_HANDLE
            readInt() // unsigned int (index)
        }

        0x17 -> {
            // string
            readInt() // unsigned int (index)
        }

        0x18 -> {
            // type
            readInt() // unsigned int (index)
        }

        0x19 -> {
            // field
            readInt() // unsigned int (index)
        }

        0x1a -> {
            // method
            readInt() // unsigned int (index)
        }

        0x1b -> {
            // enum
            readInt() // unsigned int (index)
        }

        0x1c -> {
            // array
            readEncodedArray()
        }

        0x1d -> {
            // annotation
            readEncodedAnnotation()
        }

        0x1e -> {
            // null
            null
        }

        0x1f -> {
            // boolean
            valueArg != 0
        }

        else -> {
            throw IllegalArgumentException("Unknown encoded value type: $type")
        }
    }

}

data class DexFile(
    val header: Header,
    val strings: List<String>,
    val typeIds: List<TypeId>,
    val protoIds: List<ProtoId>,
    val fieldIds: List<FieldId>,
    val methodIds: List<MethodId>,
    val classDefs: List<ClassDef>,
    val callSiteIds: List<CallSiteId>,
    val methodHandles: List<MethodHandle>,
    val data: ByteArray, // Unsigned byte
    val linkData: ByteArray, // Unsigned byte
) {

    companion object {
        fun fromFile(inputFile: RandomAccessFile): DexFile {
            val header = Header.fromDataInputStream(inputFile)
            inputFile.seek(header.stringIdsOff.toLong() and 0xFFFFFFFF)
            val stringIds = List(header.stringIdsSize) {
                inputFile.readInt()
            }
            val strings = List(header.stringIdsSize) {
                inputFile.seek(stringIds[it].toLong() and 0xFFFFFFFF)
                val length = inputFile.readUnsignedLeb128()
                val bytes = ByteArray(length)
                inputFile.read(bytes)
                String(bytes)
            }
            inputFile.seek(header.typeIdsOff.toLong() and 0xFFFFFFFF)
            val typeIds = List(header.typeIdsSize) {
                inputFile.readInt()
            }
            val typeIdItems = List(header.typeIdsSize) {
                val typeDescriptor = strings[typeIds[it]]
                TypeId(typeDescriptor)
            }
            inputFile.seek(header.protoIdsOff.toLong() and 0xFFFFFFFF)
            val protoIdItems = List(header.protoIdsSize) {
                val shortyIdx = inputFile.readInt()
                val returnTypeIdx = inputFile.readInt()
                val parametersOff = inputFile.readInt()
                ProtoId(strings[shortyIdx], typeIdItems[returnTypeIdx], parametersOff)
            }
            inputFile.seek(header.fieldIdsOff.toLong() and 0xFFFFFFFF)
            val fieldIdItems = List(header.fieldIdsSize) {
                val classIdx = inputFile.readUnsignedShort()
                val typeIdx = inputFile.readUnsignedShort()
                val nameIdx = inputFile.readInt()
                FieldId(typeIdItems[classIdx], typeIdItems[typeIdx], strings[nameIdx])
            }
            inputFile.seek(header.methodIdsOff.toLong() and 0xFFFFFFFF)
            val methodIdItems = List(header.methodIdsSize) {
                val classIdx = inputFile.readUnsignedShort()
                val protoIdx = inputFile.readUnsignedShort()
                val nameIdx = inputFile.readInt()
                MethodId(typeIdItems[classIdx], protoIdItems[protoIdx], strings[nameIdx])
            }
            inputFile.seek(header.classDefsOff.toLong() and 0xFFFFFFFF)
            val classDefs = List(header.classDefsSize) {
                val classIdx = inputFile.readInt()
                val accessFlags = inputFile.readInt()
                val superclassIdx = inputFile.readInt()
                val interfacesOff = inputFile.readInt()
                val sourceFileIdx = inputFile.readInt()
                val annotationsOff = inputFile.readInt()
                val classDataOff = inputFile.readInt()
                val staticValuesOff = inputFile.readInt()
                val superClassTypeId = if (superclassIdx == NO_INDEX) {
                    null
                } else {
                    typeIdItems[superclassIdx]
                }
                val sourceFile = if (sourceFileIdx == NO_INDEX) {
                    null
                } else {
                    strings[sourceFileIdx]
                }
                ClassDef(
                    typeIdItems[classIdx],
                    accessFlags,
                    superClassTypeId,
                    interfacesOff,
                    sourceFile,
                    annotationsOff,
                    classDataOff,
                    staticValuesOff
                )
            }
            inputFile.seek(header.mapOff.toLong() and 0xFFFFFFFF)
            val mapListSize = inputFile.readInt()
            val mapList = List(mapListSize) {
                val type = inputFile.readUnsignedShort()
                val unused = inputFile.readUnsignedShort()
                val size = inputFile.readInt()
                val offset = inputFile.readInt()
                MapItem(type, unused, size, offset)
            }
            val callSiteIdsList: List<CallSiteId> = mapList.find { it.type == MapItem.TYPE_CALL_SITE_ID_ITEM }?.run {
                inputFile.seek(offset.toLong() and 0xFFFFFFFF)
                val callSiteIds = List(size) {
                    inputFile.readInt()
                }
                List(size) {
                    val callSiteOff = callSiteIds[it]
                    inputFile.seek(callSiteOff.toLong() and 0xFFFFFFFF)
                    // encoded_array_item
                    val items = inputFile.readEncodedArray()
                    if (items.size < 3) {
                        throw IllegalArgumentException("Invalid call site item")
                    }
                    val methodHandleIdx = items[0] as Int
                    val methodName = strings[items[1] as Int]
                    val methodType = protoIdItems[items[2] as Int]
                    val argumentsToLinker = items.drop(3)
                    // What are these for?
                    CallSiteId(methodHandleIdx, methodName, methodType, argumentsToLinker)
                }
            } ?: emptyList()
            val methodHandles: List<MethodHandle> = mapList.find { it.type == MapItem.TYPE_METHOD_HANDLE_ITEM }?.run {
                inputFile.seek(offset.toLong() and 0xFFFFFFFF)
                List(size) {
                    val methodHandleType = inputFile.readUnsignedShort()
                    val unused = inputFile.readUnsignedShort()
                    val fieldOrMethodId = inputFile.readUnsignedShort()
                    val unused2 = inputFile.readUnsignedShort()
                    MethodHandle(methodHandleType, fieldOrMethodId)
                }
            } ?: emptyList()
            // TODO: class data items


            return DexFile(
                header,
                strings,
                typeIdItems,
                protoIdItems,
                fieldIdItems,
                methodIdItems,
                classDefs,
                callSiteIdsList,
                methodHandles,
                ByteArray(header.dataSize),
                ByteArray(header.linkSize),
            )
        }

        const val NO_INDEX = 0xffffffff.toInt()
    }
}