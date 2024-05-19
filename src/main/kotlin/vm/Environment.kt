package com.yhs0602.vm

import com.yhs0602.dex.*
import com.yhs0602.vm.classloader.DexClassLoader
import com.yhs0602.vm.instance.DictionaryBackedInstance
import com.yhs0602.vm.instance.MockedInstance
import com.yhs0602.vm.instance.unmarshalArgument
import com.yhs0602.vm.instruction.Instruction
import com.yhs0602.vm.type.Type

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
    init {
        if (instance != null)
            throw Exception("Environment is already initialized")
        instance = this
    }

    companion object {
        @Volatile
        private var instance: Environment? = null

        fun getInstance(): Environment {
            return instance ?: throw Exception("Environment is not initialized")
        }

        fun reset() {
            instance = null
        }
    }

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

    private val typeIds = mutableMapOf<Pair<DexFile, Int>, TypeId>()
    private val strings = mutableMapOf<Pair<DexFile, Int>, String>()
    private val classDefs = mutableListOf<ParsedClass>()

    private val staticFields = mutableMapOf<Pair<TypeId, Int>, Array<RegisterValue>>()
    private val initializedClasses: MutableSet<TypeId> = mutableSetOf()
    private val typeIdToCustomClass: MutableMap<TypeId, Class<*>> = mutableMapOf()
    private val classLoader = DexClassLoader(dexFiles, mockedClasses)

    init {
        dexFiles.forEach { dexFile ->
            dexFile.classDefs.forEach { parsedClass ->
                classDefs.add(parsedClass)
            }
        }
    }

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

    fun getType(typeId: TypeId): Type {
        return classLoader.getClass(typeId)
    }

    // If targetType is interface
    // If object is array
    // If object is subclass of targetType
    fun isInstanceOf(
        objectRef: RegisterValue.ObjectRef,
        targetTypeDescriptor: String
    ): Boolean {
        // If the target type is a primitive type, return false
        if (isPrimitiveType(targetTypeDescriptor)) return false

        // Get the actual type of the object reference
        val actualTypeDescriptor = objectRef.typeId.descriptor

        // If the actual type is the same as the target type, return true
        if (actualTypeDescriptor == targetTypeDescriptor) {
            return true
        }

        val targetType = classLoader.getClass(TypeId(targetTypeDescriptor))
        val objectType = classLoader.getClass(objectRef.typeId)
        return objectType.isAssignableTo(targetType)
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

    // TODO: New version
    fun getClassRepresentation2(typeId: TypeId, depth: Int): ClassRepresentation {
        val type = classLoader.getClass(typeId)
        TODO()
    }

    // Legacy: Returns ClassRepresentation from typeId, from dex file or mocked classes
    // Callers: Environment.loadClass, DictionaryBackedInstance.init, NewInstance instruction
    fun getClassRepresentation(typeId: TypeId, depth: Int): ClassRepresentation {
        println("Searching for class def with typeId $typeId")
        val classDef = classDefs.find {
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
            val classData = classDef.classData
            if (classData == null) {
                // Interface or abstract class
                initializedClasses.add(typeId)
                return
            }
            val clinit = classData.directMethods.find {
                it.methodId.name == "<clinit>"
            }
            if (clinit != null) {
                val clInitCodeItem = clinit.codeItem ?: error("Code item not found")
                println("Executing clinit for class ${classDef.classDef.typeId.descriptor}")
                initializedClasses.add(typeId)
                executeMethod(clInitCodeItem, this, arrayOf(), 0, depth + 1)
            } else {
                println("clinit not found for class ${classDef.classDef.typeId.descriptor}; searched: ${classData.directMethods.joinToString { it.methodId.name }}")
                initializedClasses.add(typeId)
            }
        }
    }


    fun createInstance(typeId: TypeId, depth: Int): RegisterValue.ObjectRef {
        // TODO: Check abstract class and interfaces
        val type = classLoader.getClass(typeId)
        return RegisterValue.ObjectRef(
            type,
            type.createInstance()
        )
    }

    // Old createInstnace version before using classloader; Used by NewInstance
    @Deprecated("Use new version of createInstance which uses classLoader")
    fun createInstance(clazz: ClassRepresentation, code: CodeItem, depth: Int): RegisterValue.ObjectRef {
        when (clazz) {
            is ClassRepresentation.DexClassRepresentation -> {
                val classDef = clazz.classDef
                if (classDef.accessFlags.isAbstract)
                    throw Exception("Cannot create instance of abstract class")
                val classData = clazz.classData ?: throw Exception("Class data not found")
                val type = classLoader.getClass(classDef.typeId)
                return RegisterValue.ObjectRef(
                    type,
                    DictionaryBackedInstance(
                        fields = classData.instanceFields,
                        dexClassRepresentation = clazz,
                        code = code,
                        depth = depth
                    )
                )
            }

            is ClassRepresentation.MockedClassRepresentation -> {
                val type = classLoader.getClass(clazz.mockedClass.classId)
                return RegisterValue.ObjectRef(
                    type,
                    MockedInstance(clazz.mockedClass.clazz)
                )
            }
        }
    }

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
                    classDefs.find {
                        it.classDef.typeId == fieldIdObj.classId && it.classData != null
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

    // New version of getMethod which uses classLoader
    fun getMethod(
        code: CodeItem,
        kindBBBB: Int,
        instance: RegisterValue.ObjectRef?,
        direct: Boolean,
        isSuperCall: Boolean,
        isStatic: Boolean,
    ): MethodWrapper {
        val dexFile = codeItemToDexFile[code] ?: error("Cannot find dex file for $code")
        val methodId = dexFile.methodIds[kindBBBB]
        if (isStatic) {
            // Call based on the method id
            val type = classLoader.getClass(methodId.classId)
            val method = type.getStaticMethod(methodId.toMethodTableEntry())
            return method
        }
        // Dispatch method based on invoke-kind
        if (direct || isSuperCall) {
            // Call based on the method id
            val type = classLoader.getClass(methodId.classId)
            val method = type.getDirectMethod(methodId.toMethodTableEntry())
            return method
        }
        if (instance == null) {
            throw Exception("Instance is null in virtual method call")
        }
        // invoke-virtual: Call based on the instance
        val type = classLoader.getClass(instance.typeId)
        val method = type.getVirtualMethodEntry(methodId.toMethodTableEntry())
        return type.getDirectMethod(method)
    }

    fun getMethodByName(classDef: ClassRepresentation.DexClassRepresentation, methodName: String): MethodWrapper? {
        val classData = classDef.classData ?: error("Cannot find class data for class def $classDef")
        val method = classData.directMethods.find {
            it.methodId.name == methodName
        } ?: classData.virtualMethods.find {
            it.methodId.name == methodName
        }
        if (method == null) {
            return iterateSuperClass(classDef.classDef) { superClassData ->
                val superMethod = superClassData.directMethods.find {
                    it.methodId.name == methodName
                } ?: superClassData.virtualMethods.find {
                    it.methodId.name == methodName
                }
                if (superMethod != null) {
                    MethodWrapper.Encoded(superMethod)
                } else {
                    null
                }
            }
        } else {
            return MethodWrapper.Encoded(method)
        }
    }

    fun <R> iterateSuperClass(
        classDef: ClassDef,
        iterator: (ClassData) -> R?
    ): R? {
        var superClassTypeId = classDef.superClassTypeId
        do {
            // FIXME: This does not check mocked superclasses
            val superClassDef = classDefs.find {
                it.classDef.typeId == superClassTypeId
            } ?: break
            val superClassData = superClassDef.classData ?: break
            iterator(superClassData)?.let {
                return@iterateSuperClass it
            }
            superClassTypeId = superClassDef.classDef.superClassTypeId
        } while (superClassTypeId != null)
        return null
    }

    // New: Use classLoader to load class
    fun loadClass(typeId: TypeId): Class<*> {
        val type = classLoader.getClass(typeId)
        return type.clazz
    }

    fun getMethod(methodId: MethodId): MethodWrapper {
        // assume direct
        val type = classLoader.getClass(methodId.classId)
        val method = type.getDirectMethod(methodId.toMethodTableEntry())
        return method
    }
}