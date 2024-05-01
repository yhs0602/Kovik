package com.yhs0602.vm

import com.yhs0602.dex.CodeItem
import com.yhs0602.dex.DexFile
import com.yhs0602.dex.ParsedClass
import com.yhs0602.dex.TypeId

// Class definitions, typeids, string constants, field ids, method ids, and method prototypes...
class Environment(
    val dexFiles: List<DexFile>
) {
    val codeItemToDexFile = dexFiles.flatMap { dexFile ->
        dexFile.classDefs.asSequence().flatMap { classDef ->
            classDef.classData?.run {
                sequenceOf(
                    directMethods.asSequence().map {
                        it.codeItem to dexFile
                    },
                    virtualMethods.asSequence().map {
                        it.codeItem to dexFile
                    }
                ).flatten()
            } ?: emptySequence()
        }
    }.toMap()

    val typeIds = mutableMapOf<Pair<DexFile, Int>, TypeId>()
    val strings = mutableMapOf<Pair<DexFile, Int>, String>()

    val staticFields = mutableMapOf<Pair<DexFile, Int>, Array<RegisterValue>>()

    fun getTypeId(codeItem: CodeItem, index: Int): TypeId {
        val dexFile = codeItemToDexFile[codeItem] ?: error("Cannot find dex file for $codeItem")
        return typeIds.getOrPut(dexFile to index) {
            dexFile.typeIds[index]
        }
    }

    fun getString(codeItem: CodeItem, index: Int): String {
        val dexFile = codeItemToDexFile[codeItem] ?: error("Cannot find dex file for $codeItem")
        return strings.getOrPut(dexFile to index) {
            dexFile.strings[index]
        }
    }

    fun isInstanceOf(objectRef: RegisterValue.ObjectRef, targetTypeDescriptor: String): Boolean {
        // If the target type is a primitive type, return false
        if (isPrimitiveType(targetTypeDescriptor)) return false

        // Get the actual type of the object reference
        val actualTypeDescriptor = objectRef.typeId.descriptor

        // If the actual type is the same as the target type, return true
        if (actualTypeDescriptor == targetTypeDescriptor) return true

        // Check the super types of the actual type
        return checkSuperTypes(actualTypeDescriptor, targetTypeDescriptor)
    }

    private fun isPrimitiveType(typeDescriptor: String): Boolean {
        return typeDescriptor in setOf(
            "I",
            "B",
            "C",
            "D",
            "F",
            "J",
            "S",
            "Z"
        ) // Integer, Byte, Char, Double, Float, Long, Short, Boolean
    }

    // Check the super types of the given type
    private fun checkSuperTypes(typeDescriptor: String, targetTypeDescriptor: String): Boolean {
        var currentTypeDescriptor = typeDescriptor
//        while (currentTypeDescriptor in classHierarchy) {
//            val superTypes = classHierarchy[currentTypeDescriptor] ?: break
//            if (targetTypeDescriptor in superTypes) return true
//            currentTypeDescriptor = superTypes.firstOrNull() ?: break
//        }
        return false
    }

    fun getClassDef(codeItem: CodeItem, typeId: TypeId): ParsedClass? {
        val dexFile = codeItemToDexFile[codeItem] ?: error("Cannot find dex file for $codeItem")
        return dexFile.classDefs.find {
            it.classDef.typeId == typeId
        }
    }

    fun createInstance(clazz: ParsedClass): Instance {
        val classDef = clazz.classDef
        if (classDef.accessFlags.isAbstract)
            throw Exception("Cannot create instance of abstract class")
        val classData = clazz.classData ?: throw Exception("Class data not found")
        return Instance(
            fields = classData.instanceFields,
        )
    }

    fun getStaticField(code: CodeItem, fieldId: Int): Array<RegisterValue>? {
        val dexFile = codeItemToDexFile[code] ?: error("Cannot find dex file for $code")
        val staticValue = staticFields[dexFile to fieldId] ?: return null
        return staticValue
    }

    fun setStaticField(code: CodeItem, fieldId: Int, value: Array<RegisterValue>) {
        val dexFile = codeItemToDexFile[code] ?: error("Cannot find dex file for $code")
        staticFields[dexFile to fieldId] = value
    }
}