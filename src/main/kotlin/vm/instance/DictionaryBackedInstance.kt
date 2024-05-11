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

    var interfaces: MutableList<Class<*>> = mutableListOf()

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
        // search for interfaces
        for (interfaceTypeId in dexClassRepresentation.classDef.flattenedInterfaces) {
            val interfaceClass = environment.getClassDef(code, interfaceTypeId, depth = depth)
            when (interfaceClass) {
                is ClassRepresentation.MockedClassRepresentation -> {
                    try {
                        interfaceTypeId.descriptor.let {
                            interfaces.add(
                                Class.forName(it.replace('/', '.').removePrefix("L").removeSuffix(";"))
                            )
                        }
                    } catch (e: ClassNotFoundException) {
                        println("Interface class not found: $interfaceTypeId")
                    }
                }

                is ClassRepresentation.DexClassRepresentation -> {

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

    override fun intercept(obj: Any?, methodRequested: Method?, arguments: Array<out Any>?, p3: MethodProxy?): Any? {
        // Check if this class overrides the method
        // Else call the super class method
        val methodName = methodRequested?.name ?: return null
        println("Requested class: ${methodRequested.declaringClass}")
        if (methodRequested.declaringClass == backingSuperClass) {
            val result = p3?.invokeSuper(obj, arguments)
            return result
        }
        val method = environment.getMethodByName(
            dexClassRepresentation, methodName
        )
        if (method != null) {
            // TODO: We should also check if the super method is requested or this method is a new method
            // unmarshal the arguments
            val unmarshalledObject = if (obj == null) {
                arrayOf(RegisterValue.Int(0))
            } else {
                unmarshalArguments(
                    arrayOf(obj),
                    arrayOf(obj::class.java)
                )
            }
            val unmarshalledArguments: Array<out RegisterValue> = if (arguments == null) {
                unmarshalledObject
            } else {
                (unmarshalledObject.asSequence() +
                    unmarshalArguments(
                        arguments,
                        methodRequested.parameterTypes
                    ).asSequence()
                    ).toList()
                    .toTypedArray()
            }
            println("Invoking method: $methodName ") // with args: ${unmarshalledArguments.joinToString()}
            val result = method.execute(unmarshalledArguments, environment, code, obj == null, depth + 1)
            // marshal the result
            println("Result: ${result.joinToString()}, requested return type: ${methodRequested.returnType}")
            val marshalled =  marshalArguments(
                environment,
                code,
                result.toList(),
                arrayOf(methodRequested.returnType)
            )
            println("Marshalled: ${marshalled.joinToString()}")
            return marshalled[0]
        } else {
            val result = p3?.invokeSuper(obj, arguments)
            return result
        }
    }
}