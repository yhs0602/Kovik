package com.example.sample;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* compiled from: ObjectExample.kt */
@Metadata(m29d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\b\u001a\u00020\tH\u0016R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\u0004¨\u0006\n"}, m28d2 = {"Lcom/example/sample/OverridingTest;", "", "myProp", "", "(I)V", "getMyProp", "()I", "setMyProp", "printMessage", "", "app_debug"}, m27k = 1, m26mv = {1, 9, 0}, m24xi = 48)
/* loaded from: /Users/yanghyeonseo/gitprojects/kovik/test-data/advanced/classes4.dex */
public class OverridingTest {
    public static final int $stable = 8;
    private int myProp;

    public OverridingTest() {
        this(0, 1, null);
    }

    public OverridingTest(int myProp) {
        this.myProp = myProp;
    }

    public /* synthetic */ OverridingTest(int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? 0 : i);
    }

    public final int getMyProp() {
        return this.myProp;
    }

    public final void setMyProp(int i) {
        this.myProp = i;
    }

    public void printMessage() {
        System.out.println((Object) "This is the parent class");
    }
}