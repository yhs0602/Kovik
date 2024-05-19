package com.yhs0602.vm

import com.yhs0602.dex.*
import com.yhs0602.vm.classloader.DexClassLoader
import com.yhs0602.vm.instance.ByteBuddyBackedInstance
import com.yhs0602.vm.instance.DictionaryBackedInstance
import com.yhs0602.vm.instance.MockedInstance
import com.yhs0602.vm.instance.unmarshalArgument
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
            typeId,
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
                return RegisterValue.ObjectRef(
                    classDef.typeId,
                    DictionaryBackedInstance(
                        fields = classData.instanceFields,
                        dexClassRepresentation = clazz,
                        code = code,
                        depth = depth
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

    fun getMethod(
        code: CodeItem,
        kindBBBB: Int,
        instance: RegisterValue.ObjectRef?,
        direct: Boolean,
        isSuperCall: Boolean
    ): MethodWrapper {
        val dexFile = codeItemToDexFile[code] ?: error("Cannot find dex file for $code")
        val methodId = dexFile.methodIds[kindBBBB]
        val classDef = classDefs.find {
            it.classDef.typeId == methodId.classId
        } ?: run {
            // TODO: search in the parent classes
            // TODO: Use mocked class, instead of mocked methods
            // However, we should cache the mocked method anyway, because the methodId is actually used in the code
            // In case of invoke-direct, just call the method
            // However in case of invoke-interface, we should find the appropriate method id
            // Search in mocked methods
            val triple = Triple(methodId.classId, methodId.protoId.parameters, methodId.name)
            val mocked = mockedMethods[triple]
            if (mocked != null) {
                println("Requested: $methodId, found mocked: $mocked, triple: $triple")
                // check if the codeitem is not null, handle inheritance
                // It should find the finest method
                if (direct) {
                    return MethodWrapper.Mocked(mocked)
                }
                // Find the method in the class hierarchy
                if (instance == null) {
                    throw Exception("Instance is null in virtual method call")
                }
                val value = instance.value
                if (value == null) {
                    throw Exception("Value is null in virtual method call")
                }
                when (value) {
                    is DictionaryBackedInstance -> {
                        val dexClassRepresentation = value.dexClassRepresentation
                        if (!isSuperCall) {
                            dexClassRepresentation.classData?.let {
                                val method = it.directMethods.find {
                                    it.methodId.protoId == methodId.protoId && it.methodId.name == methodId.name
                                } ?: it.virtualMethods.find {
                                    it.methodId.protoId == methodId.protoId && it.methodId.name == methodId.name
                                }
                                if (method != null && method.codeItem != null) {
                                    return MethodWrapper.Encoded(method)
                                }
                            }
                        }
                        return iterateSuperClass(dexClassRepresentation.classDef) { superClassData ->
                            val superMethod = superClassData.directMethods.find {
                                it.methodId.protoId == methodId.protoId && it.methodId.name == methodId.name
                            } ?: superClassData.virtualMethods.find {
                                it.methodId.protoId == methodId.protoId && it.methodId.name == methodId.name
                            }
                            if (superMethod != null && superMethod.codeItem != null) {
                                MethodWrapper.Encoded(superMethod)
                            } else {
                                MethodWrapper.Mocked(mocked)
                            }
                        } ?: MethodWrapper.Mocked(mocked)
                    }

                    is MockedInstance -> {
                        return MethodWrapper.Mocked(mocked)
                    }

                    is ByteBuddyBackedInstance -> TODO()
                }
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
        println("Requested: $methodId, found encoded: $method; ${method?.methodId}")
        println(classData.directMethods.joinToString { it.methodId.toString() })
        println(classData.virtualMethods.joinToString { it.methodId.toString() })
        if (method == null) {
            // try to find in superclasses
            return iterateSuperClass(classDef.classDef) { superClassData ->
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
            if (direct) {
                return MethodWrapper.Encoded(method)
            } else {
                // search for the method in the class hierarchy
                if (instance == null) {
                    throw Exception("Instance is null in virtual method call")
                }
                val value = instance.value
                if (value == null) {
                    throw Exception("Value is null in virtual method call")
                }

                when (value) {
                    is DictionaryBackedInstance -> {
                        val dexClassRepresentation = value.dexClassRepresentation
                        if (!isSuperCall) {
                            dexClassRepresentation.classData?.let {
                                val method = it.directMethods.find {
                                    it.methodId.protoId == methodId.protoId && it.methodId.name == methodId.name
                                } ?: it.virtualMethods.find {
                                    it.methodId.protoId == methodId.protoId && it.methodId.name == methodId.name
                                }
                                if (method != null && method.codeItem != null) {
                                    return MethodWrapper.Encoded(method)
                                }
                            }
                        }
                        return iterateSuperClass(dexClassRepresentation.classDef) { superClassData ->
                            val superMethod = superClassData.directMethods.find {
                                it.methodId.protoId == methodId.protoId && it.methodId.name == methodId.name
                            } ?: superClassData.virtualMethods.find {
                                it.methodId.protoId == methodId.protoId && it.methodId.name == methodId.name
                            }
                            if (superMethod != null && superMethod.codeItem != null) {
                                MethodWrapper.Encoded(superMethod)
                            } else {
                                MethodWrapper.Encoded(method)
                            }
                        } ?: MethodWrapper.Encoded(method)
                    }

                    is MockedInstance -> {
                        return MethodWrapper.Encoded(method)
                    }

                    is ByteBuddyBackedInstance -> TODO()
                }
            }
        }
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
}