package com.example.sample;

import kotlin.Metadata;

/* compiled from: WideTest.kt */
@Metadata(m29d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004¨\u0006\u0005"}, m28d2 = {"Lcom/example/sample/WideTest;", "", "()V", "testLong", "", "app_debug"}, m27k = 1, m26mv = {1, 9, 0}, m24xi = 48)
/* loaded from: /Users/yanghyeonseo/gitprojects/kovik/test-data/advanced/classes4.dex */
public final class WideTest {
    public static final int $stable = 0;

    public final void testLong() {
        System.out.println((Object) ("100 + 200 = " + (100 + 200)));
        System.out.println((Object) ("Max Long: 9223372036854775807, Min Long: -9223372036854775808"));
        System.out.println((Object) ("100000000000 + 200000000000 = " + (100000000000L + 200000000000L)));
        double doubleValue = 100.0f;
        System.out.println((Object) ("Float: 100.0, Double: " + doubleValue));
        double edgeDouble = -1.4E-45f;
        System.out.println((Object) ("Edge Float: -1.4E-45, Edge Double: " + edgeDouble));
    }
}