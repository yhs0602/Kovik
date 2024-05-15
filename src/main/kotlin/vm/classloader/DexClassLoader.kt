package com.yhs0602.vm.classloader

import com.yhs0602.dex.DexFile
import com.yhs0602.dex.ParsedClass
import com.yhs0602.dex.TypeId
import com.yhs0602.vm.GeneralMockedClass

class DexClassLoader(
    dexFiles: List<DexFile>,
    private val mockedClasses: Map<TypeId, GeneralMockedClass> = mapOf(),
) {
    // first load injected classes
    // Caches the Types that have been loaded
    // Load superclasses, instances correctly, then load subclasses
    private val loadedTypes = mutableMapOf<TypeId, Type>(
        TypeId("Ljava/lang/Object;") to ObjectType
    )

    private val loadedMockedClasses = mutableSetOf<Class<*>>()

    private fun loadClass(clazz: Class<*>): Type {
        val typeId = TypeId(clazz.descriptorString())
        loadedTypes[typeId]?.let {
            return it
        }
        val mockedClass = mockedClasses[typeId]
        return if (mockedClass != null) {
            loadClass(typeId, mockedClass)
        } else {
            loadClass(typeId, GeneralMockedClass(clazz))
        }
    }

    private fun loadClass(typeId: TypeId, mockedClass: GeneralMockedClass): Type {
        loadedTypes[typeId]?.let {
            return it
        }
        val superClass = mockedClass.clazz.superclass
        val superClassType: Type? = if (superClass != null && superClass !in loadedMockedClasses) {
            loadClass(superClass)
        } else {
            null
        }
        val interfaces = mockedClass.clazz.interfaces
        val interfaceTypes = interfaces.map {
            loadClass(it)
        }

        val loadedType = MockedType(
            mockedClass.clazz,
            superClassType,
            interfaceTypes
        )
        loadedTypes[typeId] = loadedType
        loadedMockedClasses.add(mockedClass.clazz)
        return loadedType
    }

    private fun loadClass(typeId: TypeId, parsedClass: ParsedClass): Type {
        loadedTypes[typeId]?.let {
            return it
        }
        val superClassTypeId = parsedClass.classDef.superClassTypeId
        val superClassType = if (superClassTypeId != null) {
            loadedTypes[superClassTypeId] ?: error(
                "Superclass ${superClassTypeId.descriptor} for ${parsedClass.classDef.typeId} not loaded"
            )
        } else {
            null
        }
        val interfaces = parsedClass.classDef.interfaces
        val interfaceTypes = interfaces.map {
            loadedTypes[it] ?: error("Interface ${it.descriptor} for ${parsedClass.classDef.typeId} not loaded")
        }
        val loadedType = DexDefinedType(
            parsedClass,
            superClassType,
            interfaceTypes,
            this
        )
        loadedTypes[typeId] = loadedType
        return loadedType
    }

    init {
        // load mocked classes first
        mockedClasses.forEach { (typeId, mockedClass) ->
            loadClass(typeId, mockedClass)
        }
        // load dex classes
        dexFiles.forEach { dexFile ->
            dexFile.classDefs.forEach { classDef ->
                val typeId = classDef.classDef.typeId
                loadClass(typeId, classDef)
            }
        }
    }

    fun getClass(typeId: TypeId): Type {
        return loadedTypes[typeId] ?: error("Class ${typeId.descriptor} not loaded")
    }
}