package com.yhs0602.vm.type

import com.yhs0602.dex.ParsedClass
import com.yhs0602.dex.toTypeName
import com.yhs0602.vm.Environment
import com.yhs0602.vm.MethodWrapper
import com.yhs0602.vm.classloader.DexClassLoader
import com.yhs0602.vm.classloader.DexDefinedTypeMethodDelegator
import com.yhs0602.vm.classloader.MethodTableEntry
import com.yhs0602.vm.instance.ByteBuddyBackedInstance
import com.yhs0602.vm.instance.Instance
import net.bytebuddy.ByteBuddy
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy
import net.bytebuddy.implementation.MethodDelegation


class DexDefinedType(
    val classDef: ParsedClass,
    override val directSuperClass: Type?,
    override val interfaces: List<Type>,
    val classLoader: DexClassLoader,
) : Type() {
    override val descriptor: String = classDef.classDef.typeId.descriptor
    override val interfaceTable: Map<MethodTableEntry, MethodTableEntry>
        get() = _interfaceTable
    private val _interfaceTable: MutableMap<MethodTableEntry, MethodTableEntry> = mutableMapOf()
    override val virtualTable: Map<MethodTableEntry, MethodTableEntry>
        get() = _virtualTable
    private val _virtualTable: MutableMap<MethodTableEntry, MethodTableEntry> = mutableMapOf()
    override val methods = mutableMapOf<MethodTableEntry, MethodWrapper>()
    override val constructors = mutableMapOf<MethodTableEntry, MethodWrapper>()
    override val staticMethods = mutableMapOf<MethodTableEntry, MethodWrapper>()
    override val clazz by lazy {
        makeClazz()
    }
    private var clinit: MethodTableEntry? = null
    private var hasCalledClinit = false

    init {
        // populate v-table and i-table of the class
        // First copy the super class's v-table and i-table
        if (directSuperClass != null) {
            _interfaceTable.putAll(directSuperClass.interfaceTable)
            _virtualTable.putAll(directSuperClass.virtualTable)
        }
        // update the v-table with the current class's methods
        classDef.classData?.let {
            for (method in it.virtualMethods) {
                val methodId = method.methodId
                val methodTableEntry = MethodTableEntry(
                    methodId.name,
                    methodId.protoId,
                    null
                )
                _virtualTable[methodTableEntry] = methodTableEntry
                methods[methodTableEntry] = MethodWrapper.Encoded(method)
            }
            for (method in it.directMethods) {
                val methodId = method.methodId
                val methodTableEntry = MethodTableEntry(
                    methodId.name,
                    methodId.protoId,
                    null
                )
                if (method.accessFlags.isStatic) {
                    staticMethods[methodTableEntry] = MethodWrapper.Encoded(method)
                    if (methodId.name == "<clinit>") {
                        clinit = methodTableEntry
                    }
                } else if (method.accessFlags.isConstructor) {
                    constructors[methodTableEntry] = MethodWrapper.Encoded(method)
                } else {
                    _virtualTable[methodTableEntry] = methodTableEntry
                    methods[methodTableEntry] = MethodWrapper.Encoded(method)
                }
            }
        }
        // update the i-table with the current class's interfaces
        for (interfaceTypeId in interfaces) {
            for (method in interfaceTypeId.virtualTable) {
                val methodID = method.value
                // check if my v-table has the same method
                if (_virtualTable.containsKey(methodID)) {
                    _interfaceTable[method.key] = methodID
                } else {
                    // TODO: Handle default methods
                    throw IllegalStateException(
                        "Required interface method ${method.key} not implemented in" +
                            " ${classDef.classDef.typeId.descriptor}"
                    )
                }
            }
        }
    }

    override fun callClInit() {
        if (hasCalledClinit) {
            return
        }
        clinit?.let {
            val clInitMethod = getMethod(it)
            if (clInitMethod !is MethodWrapper.Encoded) {
                throw IllegalStateException("clinit method is not encoded")
            }
            hasCalledClinit = true
            clInitMethod.execute(
                arrayOf(),
                Environment.getInstance(),
                clInitMethod.encodedMethod.codeItem ?: throw IllegalStateException("clinit method has no code"),
                true,
                0
            )
        }
    }

    private fun makeClazz(): Class<*> {
        // use bytebuddy to create a class
        var builder = ByteBuddy()
            .subclass(directSuperClass?.clazz ?: Object::class.java)
            .name(classDef.classDef.typeId.descriptor.toTypeName())
        // Declare fields
        val instanceFields = (classDef.classData?.instanceFields ?: emptyList()).asSequence()
        val staticFields = (classDef.classData?.staticFields ?: emptyList()).asSequence()
        (instanceFields + staticFields).forEach {
            val fieldId = it.fieldId
            val fieldType = classLoader.getClass(fieldId.typeId)
            builder = builder.defineField(fieldId.name, fieldType.clazz, it.accessFlags.getFlags())
        }
        // Declare constructors
        val constructors = (classDef.classData?.directMethods ?: emptyList()).asSequence()
            .filter { it.accessFlags.isConstructor }
        constructors.forEach { constructor ->
            val methodId = constructor.methodId
            val parameterTypes = methodId.protoId.parameters.map { classLoader.getClass(it) }
            val methodBuilder = builder.defineConstructor(constructor.accessFlags.getFlags())
                .withParameters(parameterTypes.map { it.clazz })
            builder = constructor.codeItem?.let {
                methodBuilder.intercept(MethodDelegation.to(DexDefinedTypeMethodDelegator(it)))
            } ?: methodBuilder.withoutCode()
        }
        // Declare method
        val directMethods = (classDef.classData?.directMethods ?: emptyList()).asSequence()
        val virtualMethods = (classDef.classData?.virtualMethods ?: emptyList()).asSequence()
        (directMethods + virtualMethods).forEach { method ->
            val methodId = method.methodId
            val protoId = methodId.protoId
            val returnType = classLoader.getClass(protoId.returnType)
            val parameterTypes = protoId.parameters.map { classLoader.getClass(it) }
            val methodBuilder = builder.defineMethod(methodId.name, returnType.clazz, method.accessFlags.getFlags())
                .withParameters(parameterTypes.map { it.clazz })
            builder = method.codeItem?.let {
                methodBuilder.intercept(MethodDelegation.to(DexDefinedTypeMethodDelegator(it)))
            } ?: methodBuilder.withoutCode()
        }
        // Implement interfaces
        builder = builder.implement(
            *interfaces.map { it.clazz }.toTypedArray()
        )

        return builder
            .make()
            .load(ClassLoader.getSystemClassLoader(), ClassLoadingStrategy.Default.INJECTION)
            .loaded
    }

    override fun createInstance(): Instance {
        return ByteBuddyBackedInstance(clazz)
    }
}