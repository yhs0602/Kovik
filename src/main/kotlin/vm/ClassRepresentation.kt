package com.yhs0602.vm

import com.yhs0602.dex.ClassData
import com.yhs0602.dex.ClassDef
import com.yhs0602.dex.TypeId

interface MockedClass {
    fun createInstance(): Any
    val classId: TypeId
}


sealed class ClassRepresentation {
    class DexClassRepresentation(val classDef: ClassDef, val classData: ClassData?) : ClassRepresentation()
    class MockedClassRepresentation(val mockedClass: MockedClass) : ClassRepresentation()
}