package com.yhs0602.vm.instance

import com.yhs0602.dex.CodeItem
import com.yhs0602.dex.TypeId
import com.yhs0602.vm.Environment
import com.yhs0602.vm.RegisterValue
import net.sf.cglib.proxy.MethodInterceptor
import net.sf.cglib.proxy.MethodProxy
import java.lang.reflect.Method
import java.lang.reflect.Proxy

sealed class Instance {
    abstract fun getField(idx: Int): Array<RegisterValue>?

    abstract fun setField(idx: Int, value: Array<RegisterValue>)
}


// TODO: Implement more strictly
fun compareMethodProto(
    method: Method,
    name: String,
    args: List<RegisterValue>,
    paramTypes: List<TypeId>
): Boolean {
    if (method.name != name) {
//        println("Method name not matched: ${method.name} != $name")
        return false
    }
    // Check instance method or static
    if (method.parameterCount != paramTypes.size) {
//        println("Parameter count not matched: ${method.parameterCount} != ${paramTypes.size}")
        return false
    }
    val methodParameterTypes = method.parameterTypes

    var i = 0
    while (i < args.size) {
        val paramType = methodParameterTypes[i]

        if (!compareProtoType(paramTypes[i], paramType)) {
            println("Parameter type not matched: ${paramTypes[i]} != $paramType")
            return false
        }
        // 파라미터의 타입과 인자의 타입을 비교하여 일치하지 않으면 false를 반환
        val (result, consumed) = compareArgumentType(args, i, paramType)
        if (!result) {
            println("Argument type not matched: ${args[i]} != $paramType")
            return false
        }
        i += consumed
    }
    return true
}

// TODO: Implement polymorphism
fun compareProtoType(typeId: TypeId, paramType: Class<*>): Boolean {
    return when {
        typeId.descriptor == "I" && paramType == Int::class.java -> true
        typeId.descriptor == "Ljava/lang/String;" && paramType == String::class.java -> true
        typeId.descriptor == "J" && paramType == Long::class.java -> true
        typeId.descriptor == "F" && paramType == Float::class.java -> true
        typeId.descriptor == "D" && paramType == Double::class.java -> true
        typeId.descriptor == "Z" && paramType == Boolean::class.java -> true
        typeId.descriptor == "C" && paramType == Char::class.java -> true
        typeId.descriptor == "B" && paramType == Byte::class.java -> true
        typeId.descriptor == "S" && paramType == Short::class.java -> true
        typeId.descriptor == "Ljava/lang/Object;" && paramType == Object::class.java -> true
        paramType.isArray && typeId.descriptor.startsWith("[") -> {
            val componentType = TypeId(typeId.descriptor.substring(1))
            compareProtoType(componentType, paramType.componentType)
        }

        paramType.descriptorString() == typeId.descriptor -> true

        else -> false
    }
}

fun compareArgumentType(args: List<RegisterValue>, idx: Int, paramType: Class<*>): Pair<Boolean, Int> {
    val arg = args[idx]
    return when {
        paramType == Int::class.java && arg is RegisterValue.Int -> true to 1
        paramType == String::class.java && arg is RegisterValue.StringRef -> true to 1
        paramType == String::class.java && arg is RegisterValue.ObjectRef -> {
            if (arg.value is MockedInstance && arg.value.value is String) {
                true to 1
            } else {
                false to 0
            }
        }

        paramType == Long::class.java && arg is RegisterValue.Int -> {
            if (args.size <= idx + 1) {
                false to 0
            }
            val nextArg = args[idx + 1]
            if (nextArg is RegisterValue.Int) {
                true to 2
            } else {
                false to 0
            }
        }

        paramType == Float::class.java && arg is RegisterValue.Int -> true to 1
        paramType == Double::class.java && arg is RegisterValue.Int -> {
            if (args.size <= idx + 1) {
                false to 0
            }
            val nextArg = args[idx + 1]
            if (nextArg is RegisterValue.Int) {
                true to 2
            } else {
                false to 0
            }
        }

        paramType == Boolean::class.java && arg is RegisterValue.Int -> true to 1
        paramType == Char::class.java && arg is RegisterValue.Int -> true to 1
        paramType == Byte::class.java && arg is RegisterValue.Int -> true to 1
        paramType == Short::class.java && arg is RegisterValue.Int -> true to 1
        paramType.isArray && arg is RegisterValue.ArrayRef -> {
            println("Array type: $paramType")
            val componentType = paramType.componentType
            val result = arg.values.all {
                compareArgumentType(listOf(it), 0, componentType).first
            }
            result to 1
        }

        paramType.isInterface && arg is RegisterValue.ObjectRef -> {
            when (val instance = arg.value) {
                null -> false to 1
                is MockedInstance -> {
                    paramType.isAssignableFrom(instance.value::class.java) to 1
                }

                is DictionaryBackedInstance -> {
                    instance.interfaces.contains(paramType) to 1
                }
            }
        }
        paramType == Class::class.java && arg is RegisterValue.ClassRef -> {
            true to 1
        }

        paramType == Object::class.java -> true to 1
        else -> false to 0
    }
}


private fun handleInterface(
    arg: RegisterValue,
    paramType: Class<*>,
    args: List<RegisterValue>,
    environment: Environment,
    code: CodeItem
) = when (arg) {
    is RegisterValue.ObjectRef -> {
        if (arg.value is MockedInstance) {
            arg.value.value to 1
        } else {
            Proxy.newProxyInstance(
                paramType.classLoader,
                arrayOf(paramType)
            ) { obj, method, proxyArgs ->
                println("Proxy call to method: ${method.name} with args: ${args.joinToString()}")
                // TODO: emulate the method and return the marshalled result
                // 1. Unmarshal arguments
                // 2. Find the method
                // 3. Execute the method
                // 4. Marshal the result
                // 5. Return the result
                null
            } to 1
        }
    }

    is RegisterValue.StringRef -> environment.getString(code, arg.index) to 1
    else -> throw IllegalArgumentException("Invalid type for interface proxy creation.")
}

class MyMethodInterceptor(
    private val environment: Environment,
    private val code: CodeItem
) : MethodInterceptor {
    override fun intercept(obj: Any?, method: Method?, args: Array<out Any>?, proxy: MethodProxy?): Any {
        // obj is the proxy object
        // method is the method
        // args is the arguments
        // proxy is the method proxy
        TODO("Not yet implemented")
        // check if the method is overridden
    }
}


