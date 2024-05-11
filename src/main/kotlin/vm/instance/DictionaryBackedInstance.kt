package com.yhs0602.vm.instance

import com.yhs0602.dex.CodeItem
import com.yhs0602.dex.EncodedField
import com.yhs0602.vm.ClassRepresentation
import com.yhs0602.vm.Environment
import com.yhs0602.vm.RegisterValue
import net.sf.cglib.proxy.MethodInterceptor
import net.sf.cglib.proxy.MethodProxy
import java.lang.reflect.Method

class DictionaryBackedInstance(
    val fields: List<EncodedField>,
    val dexClassRepresentation: ClassRepresentation.DexClassRepresentation,
    val environment: Environment,
    val code: CodeItem,
    val depth: Int
) : Instance(), MethodInterceptor {
    private val fieldValues: MutableMap<Int, Array<RegisterValue>> = List(fields.size) { idx ->
        idx to arrayOf<RegisterValue>(RegisterValue.Int(0))
    }.toMap().toMutableMap()

    var backingSuperInstance: Any? = null
    var backingOriginalSuperInstance: Any? = null
    var backingSuperClass: Class<*>? = null
        private set

    init {
        var superClassTypeId = dexClassRepresentation.classDef.superClassTypeId
        while (superClassTypeId != null) {
            val superClass = environment.getClassDef(code, superClassTypeId, depth = depth)
            when (superClass) {
                is ClassRepresentation.MockedClassRepresentation -> {
                    try {
                        superClassTypeId.descriptor.let {
                            backingSuperClass = Class.forName(it.replace('/', '.').removePrefix("L").removeSuffix(";"))
                        }
                        break
                    } catch (e: ClassNotFoundException) {
                        println("Super class not found: $superClassTypeId")
                        backingSuperClass = null
                    }
                }

                is ClassRepresentation.DexClassRepresentation -> {
                    superClassTypeId = superClass.classDef.superClassTypeId
                }
            }
        }
    }

    override fun getField(idx: Int): Array<RegisterValue>? {
        // first check the local fields
        // then check the super class fields
        val localFieldValue = fieldValues[idx]
        if (localFieldValue != null) {
            return localFieldValue
        }
//        iterateSuperClass(dexClassRepresentation, dexFile) {
//            val superInstance = backingSuperInstance
//            if (superInstance is Instance) {
//                return superInstance.getField(idx)
//            }
//        }
        return null
    }

    override fun setField(idx: Int, value: Array<RegisterValue>) {
        fieldValues[idx] = value
    }

    override fun toString(): String {
        return "DictionaryBackedInstance(${fieldValues.values.joinToString { it.contentToString() }})"
    }

    override fun intercept(p0: Any?, p1: Method?, p2: Array<out Any>?, p3: MethodProxy?): Any? {
        // Check if this class overrides the method
        // Else call the super class method
//        println("obj class=${p0?.javaClass}")
//        println("proxy class=${p3?.javaClass}")
//        println("backing class=${backingSuperInstance?.javaClass}")
//        println("backingbacking class=${backingOriginalSuperInstance?.javaClass}")
        println("p1: $p1, p2: ${p2?.joinToString()}")
        val result = p3?.invokeSuper(p0, p2)
//        println("result=$result")
        return result
    }
}