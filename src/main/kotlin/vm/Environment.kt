package com.yhs0602.vm

import com.yhs0602.dex.CodeItem
import com.yhs0602.dex.DexFile
import com.yhs0602.dex.TypeId
import com.yhs0602.vm.instruction.Instruction

// Class definitions, typeids, string constants, field ids, method ids, and method prototypes...
class Environment(
    val dexFiles: List<DexFile>,
    val mockedMethods: Map<Triple<TypeId, List<TypeId>, String>, MockedMethod> = mapOf(),
    val mockedClasses: Map<TypeId, MockedClass> = mapOf(),
    val beforeInstruction: (Int, Instruction, Memory) -> Unit = { pc: Int, instruction: Instruction, memory: Memory -> },
    val afterInstruction: (Int, Instruction, Memory) -> Unit = { pc: Int, instruction: Instruction, memory: Memory -> }
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

    fun getClassDef(codeItem: CodeItem, typeId: TypeId): ClassRepresentation? {
        val dexFile = codeItemToDexFile[codeItem] ?: error("Cannot find dex file for $codeItem")
        println("Searching for class def with typeId $typeId")
        val classDef = dexFile.classDefs.find {
            it.classDef.typeId == typeId
        }
        if (classDef != null)
            return ClassRepresentation.DexClassRepresentation(classDef.classDef, classDef.classData)
//        println("Class def not found in dex file ${dexFile.file.name}")
        for (file in dexFiles) {
//            println("Searching in dex file ${file.file.name}")
            val classDef = file.classDefs.find {
                it.classDef.typeId == typeId
            }
            if (classDef != null)
                return ClassRepresentation.DexClassRepresentation(classDef.classDef, classDef.classData)
        }
        println("Class def not found in any dex file")
        // search for mocked classes
        mockedClasses[typeId]?.let {
            return ClassRepresentation.MockedClassRepresentation(it)
        }
        return null
    }

    fun createInstance(clazz: ClassRepresentation): RegisterValue.ObjectRef {
        when (clazz) {
            is ClassRepresentation.DexClassRepresentation -> {
                val classDef = clazz.classDef
                if (classDef.accessFlags.isAbstract)
                    throw Exception("Cannot create instance of abstract class")
                val classData = clazz.classData ?: throw Exception("Class data not found")
                return RegisterValue.ObjectRef(
                    classDef.typeId,
                    DictionaryBackedInstance(
                        fields = classData.instanceFields,
                    )
                )
            }

            is ClassRepresentation.MockedClassRepresentation -> {
                return RegisterValue.ObjectRef(
                    clazz.mockedClass.classId,
                    MockedInstance(clazz.mockedClass.createInstance())
                )
            }
        }
    }

    fun getStaticField(code: CodeItem, fieldId: Int): Array<RegisterValue>? {
        val dexFile = codeItemToDexFile[code] ?: error("Cannot find dex file for $code")
        val fieldIdObj = dexFile.fieldIds[fieldId]
        println("Getting static field $fieldIdObj")
        val staticValue = staticFields[dexFile to fieldId] ?: return null
        return staticValue
    }

    fun setStaticField(code: CodeItem, fieldId: Int, value: Array<RegisterValue>) {
        val dexFile = codeItemToDexFile[code] ?: error("Cannot find dex file for $code")
        staticFields[dexFile to fieldId] = value
    }

    fun getMethod(code: CodeItem, kindBBBB: Int): MethodWrapper {
        val dexFile = codeItemToDexFile[code] ?: error("Cannot find dex file for $code")
        val methodId = dexFile.methodIds[kindBBBB]
        val classDef = dexFile.classDefs.find {
            it.classDef.typeId == methodId.classId
        } ?: run {
            // TODO: search in the parent classes
            // Search in mocked
            val triple = Triple(methodId.classId, methodId.protoId.parameters, methodId.name)
            val mocked = mockedMethods[triple]
            if (mocked != null) {
                return MethodWrapper.Mocked(mocked)
            }
            // Dump the class def
            dexFile.classDefs.forEach {
                println(it)
            }
            throw Exception("Cannot find class def for method id $methodId, ${methodId.classId}")
        }
        val classData = classDef.classData ?: error("Cannot find class data for class def $classDef")
        val method = classData.directMethods.find {
            it.methodId == methodId
        } ?: classData.virtualMethods.find {
            it.methodId == methodId
        } ?: error("Cannot find method for method id $methodId")
        // method.codeItem
        return MethodWrapper.Encoded(method)
    }

    fun executeMockedMethod(
        code: CodeItem,
        method: MockedMethod,
        registers: Array<RegisterValue>,
        c: Int
    ): Array<RegisterValue> {
        // marshal the arguments
        val args = registers.copyOfRange(0, c)
        // convert the arguments to the expected types
        println("Executing mocked method $method with args ${args.joinToString()}")
        return method.execute(args, this, code)
    }
}