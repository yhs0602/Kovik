package com.yhs0602.vm

import com.yhs0602.dex.*
import com.yhs0602.vm.instruction.Instruction

// Class definitions, typeids, string constants, field ids, method ids, and method prototypes...
class Environment(
    val dexFiles: List<DexFile>,
    val mockedMethods: Map<Triple<TypeId, List<TypeId>, String>, MockedMethod> = mapOf(),
    val mockedClasses: Map<TypeId, GeneralMockedClass> = mapOf(),
    val beforeInstruction: (Int, Instruction, Memory, Int) -> Unit = { pc: Int, instruction: Instruction, memory: Memory, depth: Int ->
    },
    val afterInstruction: (Int, Instruction, Memory, Int) -> Unit = { pc: Int, instruction: Instruction, memory: Memory, depth: Int ->
    }
) {
    private val codeItemToDexFile = dexFiles.flatMap { dexFile ->
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

    val staticFields = mutableMapOf<Pair<TypeId, Int>, Array<RegisterValue>>()
    val initializedClasses: MutableSet<TypeId> = mutableSetOf()

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
    fun isInstanceOf(
        codeItem: CodeItem,
        objectRef: RegisterValue.ObjectRef,
        targetTypeDescriptor: String,
        depth: Int
    ): Boolean {
        // If the target type is a primitive type, return false
        if (isPrimitiveType(targetTypeDescriptor)) return false

        // Get the actual type of the object reference
        val actualTypeDescriptor = objectRef.typeId.descriptor

        // If the actual type is the same as the target type, return true
        if (actualTypeDescriptor == targetTypeDescriptor) return true

        // Check the interface

        // Check the super types of the actual type
        return checkSuperTypes(codeItem, actualTypeDescriptor, targetTypeDescriptor, depth)
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
    private fun checkSuperTypes(
        codeItem: CodeItem,
        typeDescriptor: String,
        targetTypeDescriptor: String,
        depth: Int
    ): Boolean {
        var currentType: TypeId? = TypeId(typeDescriptor)

        // Get the class def of the class of typeDescriptor
        val classDef = getClassDef(codeItem, TypeId(typeDescriptor), depth) ?: return false
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

    fun getClassDef(codeItem: CodeItem, typeId: TypeId, depth: Int): ClassRepresentation {
        val dexFile = codeItemToDexFile[codeItem] ?: error("Cannot find dex file for $codeItem")
//        println("Searching for class def with typeId $typeId")
        val classDef = dexFile.classDefs.find {
            it.classDef.typeId == typeId
        }
        if (classDef != null) {
            checkStaticInit(typeId, classDef, depth)
            return ClassRepresentation.DexClassRepresentation(classDef.classDef, classDef.classData)
        }
//        println("Class def not found in dex file ${dexFile.file.name}")
        for (file in dexFiles) {
//            println("Searching in dex file ${file.file.name}")
            val classDef = file.classDefs.find {
                it.classDef.typeId == typeId
            }
            if (classDef != null) {
                checkStaticInit(typeId, classDef, depth)
                return ClassRepresentation.DexClassRepresentation(classDef.classDef, classDef.classData)
            }
        }
//        println("Class def not found in any dex file, using mocked version")
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

    private fun checkStaticInit(typeId: TypeId, classDef: ParsedClass, depth: Int) {
        if (typeId !in initializedClasses) {
            // call clinit
            val classData = classDef.classData ?: error("Class data not found")
            val clinit = classData.directMethods.find {
                it.methodId.name == "<clinit>"
            }
            if (clinit != null) {
                val clInitCodeItem = clinit.codeItem ?: error("Code item not found")
                println("Executing clinit for class ${classDef.classDef.typeId.descriptor}")
                executeMethod(clInitCodeItem, this, arrayOf(), 0, depth + 1)
            } else {
                println("clinit not found for class ${classDef.classDef.typeId.descriptor}; searched: ${classData.directMethods.joinToString { it.methodId.name }}")
            }
            initializedClasses.add(typeId)
        }
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
                        dexClassRepresentation = clazz
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

    // TODO: Call clinit first
    fun getStaticField(code: CodeItem, fieldId: Int, depth: Int): Array<RegisterValue> {
        val dexFile = codeItemToDexFile[code] ?: error("Cannot find dex file for $code")
        val fieldIdObj = dexFile.fieldIds[fieldId]
        // Check if the class is mocked or not
        val staticValue = staticFields[fieldIdObj.classId to fieldId]
//        println("Getting static field $fieldIdObj")
        if (staticValue == null) { // Mocked or non-existent field
            // Mocked classes
            val mocked = mockedClasses[fieldIdObj.classId]
            if (mocked != null) {
//                println("Requested: $fieldIdObj, found: $mocked")
                val declaredFields = mocked.clazz.declaredFields
                declaredFields.find {
                    it.name == fieldIdObj.name
                }?.let {
//                    println("Found field: ${it.name} ${it.type} ")
                    return unmarshalArgument(mocked.clazz.getField(it.name).get(null), it.type)
                }
//                println("Requested: $fieldIdObj, not found")
                return arrayOf()
            } else {
//                println("Requested: $fieldIdObj, not found")
                checkStaticInit(
                    fieldIdObj.classId,
                    dexFile.classDefs.find {
                        it.classDef.typeId == fieldIdObj.classId
                    } ?: error("Cannot find class def for field id $fieldIdObj"),
                    depth
                )
                return staticFields.getOrPut(fieldIdObj.classId to fieldId) {
                    arrayOf(RegisterValue.Int(0)) // Default value of the static field is 0!
                }
            }
        }
        return staticValue
    }

    fun setStaticField(code: CodeItem, fieldId: Int, value: Array<RegisterValue>) {
        val dexFile = codeItemToDexFile[code] ?: error("Cannot find dex file for $code")
        val fieldIdObj = dexFile.fieldIds[fieldId]
        staticFields[fieldIdObj.classId to fieldId] = value
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
        }
//        ?: error("Cannot find method for method id from" +
//            " $classDef,  $methodId:")
//            " ${classData.directMethods.joinToString { it.methodId.toString() }}" +
//            " ${classData.virtualMethods.joinToString { it.methodId.toString() }}")
        // There is in AccessTest, but not in InheritanceTest
        // TODO: find in super classes
        // method.codeItem
        if (method == null) {
            // try to find in superclasses
            return iterateSuperClass(classDef, dexFile) { superClassData ->
                val superMethod = superClassData.directMethods.find {
                    it.methodId.protoId == methodId.protoId && it.methodId.name == methodId.name
                } ?: superClassData.virtualMethods.find {
                    it.methodId.protoId == methodId.protoId && it.methodId.name == methodId.name
                }
                if (superMethod != null) {
                    MethodWrapper.Encoded(superMethod)
                } else {
                    null
                }
            } ?: error("Cannot find method for method id $methodId")
        } else {
            return MethodWrapper.Encoded(method)
        }
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
//        println("Executing mocked method $method with args ${args.joinToString(",")}")
        return method.execute(args, this, code, isStatic)
    }

}

fun <R> iterateSuperClass(
    classDef: ParsedClass,
    dexFile: DexFile,
    iterator: (ClassData) -> R?
): R? {
    var superClass = classDef.classDef.superClassTypeId
    do {
        val superClassDef = dexFile.classDefs.find {
            it.classDef.typeId == superClass
        } ?: break
        val superClassData = superClassDef.classData ?: break
        iterator(superClassData)?.let {
            return@iterateSuperClass it
        }
        superClass = superClassDef.classDef.superClassTypeId
    } while (superClass != null)
    return null
}