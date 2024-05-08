package com.yhs0602.vm

import com.yhs0602.dex.CodeItem
import com.yhs0602.dex.DexFile
import com.yhs0602.dex.TypeId
import com.yhs0602.vm.instruction.Instruction

// Class definitions, typeids, string constants, field ids, method ids, and method prototypes...
class Environment(
    val dexFiles: List<DexFile>,
    val mockedMethods: Map<Triple<TypeId, List<TypeId>, String>, MockedMethod> = mapOf(),
    val mockedClasses: Map<TypeId, GeneralMockedClass> = mapOf(),
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

    // If targetType is interface
    // If object is array
    // If object is subclass of targetType
    fun isInstanceOf(codeItem: CodeItem, objectRef: RegisterValue.ObjectRef, targetTypeDescriptor: String): Boolean {
        // If the target type is a primitive type, return false
        if (isPrimitiveType(targetTypeDescriptor)) return false

        // Get the actual type of the object reference
        val actualTypeDescriptor = objectRef.typeId.descriptor

        // If the actual type is the same as the target type, return true
        if (actualTypeDescriptor == targetTypeDescriptor) return true

        // Check the interface

        // Check the super types of the actual type
        return checkSuperTypes(codeItem, actualTypeDescriptor, targetTypeDescriptor)
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
    private fun checkSuperTypes(codeItem: CodeItem, typeDescriptor: String, targetTypeDescriptor: String): Boolean {
        var currentType: TypeId? = TypeId(typeDescriptor)

        // Get the class def of the class of typeDescriptor
        val classDef = getClassDef(codeItem, TypeId(typeDescriptor)) ?: return false
        // Check interfaces first
        when (classDef) {
            is ClassRepresentation.DexClassRepresentation -> {
                classDef.classDef.flattendInterfaces.forEach {
                    if (it.descriptor == targetTypeDescriptor) {
                        return true
                    }
                }
            }

            is ClassRepresentation.MockedClassRepresentation -> {
                TODO()
            }
        }
        do {
            // Get the super class of the class
            if (currentType?.descriptor == targetTypeDescriptor) {
                return true
            }
            when (classDef) {
                is ClassRepresentation.DexClassRepresentation -> {
                    currentType = classDef.classDef.superClassTypeId
                }

                is ClassRepresentation.MockedClassRepresentation -> {
                    currentType = classDef.mockedClass.classId
                }
            }
        } while (currentType != null)
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
        println("Class def not found in any dex file, using mocked version")
        // search for mocked classes
        mockedClasses[typeId]?.let {
            return ClassRepresentation.MockedClassRepresentation(it)
        }
        throw Exception(
            "Cannot find class def for type id $typeId: mockedClasses: ${
                mockedClasses.entries.joinToString {
                    "${it.key} -> ${it.value}"
                }
            }"
        )
//        return null
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
                    MockedInstance(clazz.mockedClass.clazz)
                )
            }
        }
    }

    // TODO: Handle Mocking
    fun getStaticField(code: CodeItem, fieldId: Int): Array<RegisterValue> {
        val dexFile = codeItemToDexFile[code] ?: error("Cannot find dex file for $code")
        val fieldIdObj = dexFile.fieldIds[fieldId]
        println("Getting static field $fieldIdObj")
        val staticValue = staticFields[dexFile to fieldId]
        if (staticValue == null) {
            // Mocked classes
            val mocked = mockedClasses[fieldIdObj.classId]
            if (mocked != null) {
                println("Requested: $fieldIdObj, found: $mocked")
                val declaredFields = mocked.clazz.declaredFields
                declaredFields.find {
                     it.name == fieldIdObj.name
                }?.let {
                    println("Found field: ${it.name} ${it.type} ")
                    return unmarshalArgument(mocked.clazz.getField(it.name).get(null), it.type)
                }
                println("Requested: $fieldIdObj, not found")
                return arrayOf()
            } else {
                println("Requested: $fieldIdObj, not found")
                return arrayOf()
            }
        }
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
            // TODO: Use mocked class, instead of mocked methods
            // However, we should cache the mocked method anyway, becuase the methodId is actually used in the code
            // In case of invoke-direct, just call the method
            // However in case of invoke-interface, we should find the appropriate method id
            // Search in mocked methods
            val triple = Triple(methodId.classId, methodId.protoId.parameters, methodId.name)
            val mocked = mockedMethods[triple]
            if (mocked != null) {
                println("Requested: $methodId, found: $mocked, triple: $triple")
                return MethodWrapper.Mocked(mocked)
            } else {
                // (TypeId(descriptor=Ljava/lang/StringBuilder;), [TypeId(descriptor=Ljava/lang/String;)], append)

                println("Requested: $methodId, not found, triple: $triple")
                println("Mocked methods: ${mockedMethods.keys.joinToString { it.toString() }}")
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
        c: Int,
        isStatic: Boolean
    ): Array<RegisterValue> {
        // marshal the arguments
        val args = registers.copyOfRange(0, c)
        // convert the arguments to the expected types
        println("Executing mocked method $method with args ${args.joinToString(",")}")
        return method.execute(args, this, code, isStatic)
    }

}