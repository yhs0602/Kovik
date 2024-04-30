package com.yhs0602.dex

class AccessFlags(private var flags: Int = 0) {
    fun addFlag(flag: Int) {
        flags = flags or flag
    }

    fun removeFlag(flag: Int) {
        flags = flags and flag.inv()
    }

    fun toggleFlag(flag: Int) {
        flags = flags xor flag
    }

    fun hasFlag(flag: Int): Boolean {
        return (flags and flag) != 0
    }

    // 접근성 확인 메서드 추가
    val isPublic: Boolean
        get() = hasFlag(0x1) // ACC_PUBLIC

    val isPrivate: Boolean
        get() = hasFlag(0x2) // ACC_PRIVATE

    val isProtected: Boolean
        get() = hasFlag(0x4) // ACC_PROTECTED

    val isStatic: Boolean
        get() = hasFlag(0x8) // ACC_STATIC

    val isFinal: Boolean
        get() = hasFlag(0x10) // ACC_FINAL

    val isSynchronized: Boolean
        get() = hasFlag(0x20) // ACC_SYNCHRONIZED

    val isVolatile: Boolean
        get() = hasFlag(0x40) // ACC_VOLATILE for fields, ACC_BRIDGE for methods

    val isBridge: Boolean
        get() = hasFlag(0x40) // ACC_VOLATILE for fields, ACC_BRIDGE for methods

    val isTransient: Boolean
        get() = hasFlag(0x80) // ACC_TRANSIENT

    val isVarArgs: Boolean
        get() = hasFlag(0x80) // ACC_VARARGS for methods: 마지막 인수는 컴파일러에 의해 'rest' 인수로 처리되어야 함

    val isNative: Boolean
        get() = hasFlag(0x100) // ACC_NATIVE

    val isInterface: Boolean
        get() = hasFlag(0x200) // ACC_INTERFACE

    val isAbstract: Boolean
        get() = hasFlag(0x400) // ACC_ABSTRACT: abstract: 이 클래스에서 구현되지 않음

    val isStrict: Boolean
        get() = hasFlag(0x800) // ACC_STRICT: strictfp: 부동 소수점 산술에 관한 엄격한 규칙

    val isSynthetic: Boolean
        get() = hasFlag(0x1000) // ACC_SYNTHETIC: 소스 코드에 직접 정의되지 않음

    val isAnnotation: Boolean
        get() = hasFlag(0x2000) // ACC_ANNOTATION

    val isEnum: Boolean
        get() = hasFlag(0x4000) // ACC_ENUM

    val isConstructor: Boolean
        get() = hasFlag(0x10000) // ACC_CONSTRUCTOR: constructor: 생성자

    val isDeclaredSynchronized: Boolean
        get() = hasFlag(0x20000) // ACC_DECLARED_SYNCHRONIZED: synchronized: 메서드에 대한 모니터 동기화가 메서드 선언에 의해 지정됨
    // 참고: 이는 실행에 영향을 미치지 않습니다(이 플래그 자체를 반영하는 경우는 제외).

    override fun toString(): String {
        val builder = StringBuilder("[AccessFlags:")
        if (isPublic) builder.append("public ")
        if (isPrivate) builder.append("private ")
        if (isProtected) builder.append("protected ")
        if (isStatic) builder.append("static ")
        if (isFinal) builder.append("final ")
        if (isSynchronized) builder.append("synchronized ")
        if (isVolatile) builder.append("volatile ")
        if (isBridge) builder.append("bridge ")
        if (isTransient) builder.append("transient ")
        if (isVarArgs) builder.append("varargs ")
        if (isNative) builder.append("native ")
        if (isInterface) builder.append("interface ")
        if (isAbstract) builder.append("abstract ")
        if (isStrict) builder.append("strict ")
        if (isSynthetic) builder.append("synthetic ")
        if (isAnnotation) builder.append("annotation ")
        if (isEnum) builder.append("enum ")
        if (isConstructor) builder.append("constructor ")
        if (isDeclaredSynchronized) builder.append("declared_synchronized ")
        builder.append("]")
        return builder.toString()
    }
}
