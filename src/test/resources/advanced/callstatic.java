package com.example.sample;

public class CallStatic {
    void callStaticMethod() {
        StaticExample.realStaticMethod();
        StaticExample.Companion.realStaticMethod();
        StaticExample.Companion.printMessage("Hello from Java");
    }

    static void JavaStaticMethod() {
        System.out.println("JavaStaticMethod");
    }

    static void test() {
        CallStatic.JavaStaticMethod();
        new CallStatic().callStaticMethod();
    }
}
