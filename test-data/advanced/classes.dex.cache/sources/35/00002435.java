package com.example.sample;

import androidx.autofill.HintConstants;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ObjectExample.kt */
@Metadata(m29d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0003¢\u0006\u0002\u0010\bJ\u0006\u0010\u000b\u001a\u00020\fJ\u0006\u0010\r\u001a\u00020\fR\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u000e"}, m28d2 = {"Lcom/example/sample/InheritanceTest;", "Lcom/example/sample/AccessTest;", HintConstants.AUTOFILL_HINT_NAME, "", "age", "", "id", "address", "(Ljava/lang/String;IILjava/lang/String;)V", "getAddress", "()Ljava/lang/String;", "printAddress", "", "printAll", "app_debug"}, m27k = 1, m26mv = {1, 9, 0}, m24xi = 48)
/* loaded from: /Users/yanghyeonseo/gitprojects/kovik/test-data/advanced/classes4.dex */
public final class InheritanceTest extends AccessTest {
    public static final int $stable = 0;
    private final String address;

    public final String getAddress() {
        return this.address;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public InheritanceTest(String name, int age, int id, String address) {
        super(name, age, id, 0, 8, null);
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(address, "address");
        this.address = address;
    }

    public final void printAddress() {
        System.out.println((Object) ("Address: " + this.address));
    }

    public final void printAll() {
        System.out.println((Object) ("Name: " + getName()));
        printAge();
        printId();
        printAddress();
    }
}