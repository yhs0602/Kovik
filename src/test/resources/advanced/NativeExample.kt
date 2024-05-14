package com.example.sample

class NativeExample {
    private external fun testNative(): String

    companion object {
        init {
            System.loadLibrary("native-lib")
        }

        fun doTest() {
            println(NativeExample().testNative())
        }
    }
}