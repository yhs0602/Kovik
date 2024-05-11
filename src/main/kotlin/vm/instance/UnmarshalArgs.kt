package com.yhs0602.vm.instance

import com.yhs0602.dex.TypeId
import com.yhs0602.vm.RegisterValue

fun unmarshalArguments(
    args: Array<out Any?>,
    paramTypes: Array<Class<*>>,
): Array<RegisterValue> {
    return args.zip(paramTypes).flatMap { (value, paramType) ->
        unmarshalArgument(value, paramType).toList()
    }.toTypedArray()
}

fun unmarshalArgument(value: Any?, returnType: Class<*>): Array<RegisterValue> {
    return when (value) {
        null -> arrayOf()
        is Int -> arrayOf(RegisterValue.Int(value))
        is Long -> {
            val low = value.toInt()
            val high = (value shr 32).toInt()
            arrayOf(RegisterValue.Int(low), RegisterValue.Int(high))
        }

        is String -> arrayOf(
            RegisterValue.ObjectRef(
                TypeId("Ljava/lang/String;"),
                MockedInstance(String::class.java).apply {
                    this.value = value
                })
        )

        is Array<*> -> {
            val values = value.flatMap {
                unmarshalArgument(it!!, returnType.componentType!!).toList()
            }
            arrayOf(
                RegisterValue.ArrayRef(
                    TypeId(returnType.componentType!!.typeName),
                    value.size,
                    values.toTypedArray()
                )
            )
        }

        else -> arrayOf(
            RegisterValue.ObjectRef(
                TypeId(value::class.javaObjectType.typeName),
                MockedInstance(value::class.javaObjectType).apply {
                    this.value = value
                })
        )
    }
}