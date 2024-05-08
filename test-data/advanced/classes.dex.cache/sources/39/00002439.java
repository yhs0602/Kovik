package com.example.sample;

import java.io.File;
import kotlin.Metadata;

/* compiled from: ObjectExample.kt */
@Metadata(m29d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u001a\u0006\u0010\u0000\u001a\u00020\u0001¨\u0006\u0002"}, m28d2 = {"testObjects", "", "app_debug"}, m27k = 2, m26mv = {1, 9, 0}, m24xi = 48)
/* loaded from: /Users/yanghyeonseo/gitprojects/kovik/test-data/advanced/classes4.dex */
public final class ObjectExampleKt {
    public static final void testObjects() {
        Point point = new Point(3, -4);
        System.out.println((Object) ("L0 Norm: " + point.L0Norm()));
        System.out.println((Object) ("L1 Norm: " + point.L1Norm()));
        Point point2 = new Point(5);
        System.out.println((Object) ("L0 Norm: " + point2.L0Norm()));
        ObjectExample accessTest = new ObjectExample("John", 25, 123);
        accessTest.printAge();
        InheritanceTest inheritanceTest = new InheritanceTest("John", 25, 123, "123 Main St");
        inheritanceTest.printAll();
        OverridingTest overridingTest = new OverridingTest();
        overridingTest.printMessage();
        ChildOverridingTest childOverridingTest = new ChildOverridingTest();
        childOverridingTest.printMessage();
        ChildSuperCallingTest childSuperCallingTest = new ChildSuperCallingTest();
        childSuperCallingTest.printMessage();
        MockedSuperTest mockedSuperTest = new MockedSuperTest("path", 10);
        System.out.println(mockedSuperTest.exists());
        System.out.println(mockedSuperTest.compareTo(new File("path")));
        System.out.println((Object) mockedSuperTest.toString());
    }
}