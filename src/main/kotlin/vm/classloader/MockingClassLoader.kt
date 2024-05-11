package com.yhs0602.vm.classloader

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class MockingClassLoader : ClassLoader() {
    override fun findClass(name: String?): Class<*> {
        return defineHelloClass()
    }

    private fun defineHelloClass(): Class<*> {
        val cw: ClassWriter = ClassWriter(0)
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "DynamicHelloWorld", null, "java/lang/Object", null)
        var mv: MethodVisitor = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null)
        mv.visitCode()
        mv.visitVarInsn(Opcodes.ALOAD, 0)
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
        mv.visitInsn(Opcodes.RETURN)
        mv.visitMaxs(1, 1)
        mv.visitEnd()
        // hello method
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "hello", "()V", null, null)
        mv.visitCode()
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
        mv.visitLdcInsn("Hello")
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false)
        mv.visitInsn(Opcodes.RETURN)
        mv.visitMaxs(2, 1)
        mv.visitEnd()

        cw.visitEnd()
        val b = cw.toByteArray()
        return defineClass("DynamicHelloWorld", b, 0, b.size)
    }
}