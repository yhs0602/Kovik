package com.example.sample;

/* loaded from: /Users/yanghyeonseo/gitprojects/kovik/test-data/advanced/classes4.dex */
public class CallStatic {
    void callStaticMethod() {
        StaticExample.realStaticMethod();
        StaticExample.Companion.realStaticMethod();
        StaticExample.Companion.printMessage("Hello from Java");
    }

    static void JavaStaticMethod() {
        System.out.println("JavaStaticMethod");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void test() {
        JavaStaticMethod();
        callStaticMethod();
    }
}