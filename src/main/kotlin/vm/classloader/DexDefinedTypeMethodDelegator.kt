package com.yhs0602.vm.classloader

import com.yhs0602.dex.CodeItem
import com.yhs0602.vm.Environment
import com.yhs0602.vm.RegisterValue
import com.yhs0602.vm.executeMethod
import com.yhs0602.vm.instance.marshalArguments
import com.yhs0602.vm.instance.unmarshalArguments
import net.bytebuddy.implementation.bind.annotation.AllArguments
import net.bytebuddy.implementation.bind.annotation.Origin
import net.bytebuddy.implementation.bind.annotation.RuntimeType
import net.bytebuddy.implementation.bind.annotation.This
import java.lang.reflect.Method

class DexDefinedTypeMethodDelegator(
    private val codeItem: CodeItem,
) {
    @RuntimeType
    fun intercept(@This instance: Any?, @Origin method: Method, @AllArguments args: Array<Any>): Any? {
        // Handle call by method name
        // if instance is null, it is a static method
        // if instance is not null, it is an instance method
        // Marshall this and args to the actual method
        val unmarshalledObject = if (instance == null) {
            arrayOf(RegisterValue.Int(0))
        } else {
            unmarshalArguments(
                arrayOf(instance),
                arrayOf(instance::class.java)
            )
        }
        val unmarshalledArguments: Array<out RegisterValue> = (unmarshalledObject.asSequence() +
            unmarshalArguments(
                args,
                method.parameterTypes
            ).asSequence()
            ).toList()
            .toTypedArray()

        println("Invoking method: ${method.name} ") // with args: ${unmarshalledArguments.joinToString()}
        val result = executeMethod(
            codeItem,
            Environment.getInstance(),
            unmarshalledArguments,
            unmarshalledArguments.size,
            0 // TODO: depth
        )
        val marshalled =  marshalArguments(
            Environment.getInstance(),
            codeItem,
            result.toList(),
            arrayOf(method.returnType)
        )
        println("Marshalled: ${marshalled.joinToString()}")
        return marshalled.firstOrNull()
    }
}