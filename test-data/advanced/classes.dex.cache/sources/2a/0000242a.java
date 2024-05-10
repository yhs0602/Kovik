package com.example.sample;

import androidx.autofill.HintConstants;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ObjectExample.kt */
@Metadata(m29d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0017\u0018\u00002\u00020\u0001B'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0005¢\u0006\u0002\u0010\bJ\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\u0012\u001a\u00020\u0011R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\u00020\u0005X\u0084\u0004¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u001a\u0010\u0007\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\n\"\u0004\b\u000e\u0010\u000f¨\u0006\u0013"}, m28d2 = {"Lcom/example/sample/AccessTest;", "", HintConstants.AUTOFILL_HINT_NAME, "", "age", "", "id", "variable", "(Ljava/lang/String;III)V", "getId", "()I", "getName", "()Ljava/lang/String;", "getVariable", "setVariable", "(I)V", "printAge", "", "printId", "app_debug"}, m27k = 1, m26mv = {1, 9, 0}, m24xi = 48)
/* loaded from: /Users/yanghyeonseo/gitprojects/kovik/test-data/advanced/classes4.dex */
public class AccessTest {
    public static final int $stable = 8;
    private final int age;

    /* renamed from: id */
    private final int f186id;
    private final String name;
    private int variable;

    public AccessTest(String name, int age, int id, int variable) {
        Intrinsics.checkNotNullParameter(name, "name");
        this.name = name;
        this.age = age;
        this.f186id = id;
        this.variable = variable;
    }

    public /* synthetic */ AccessTest(String str, int i, int i2, int i3, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, i, i2, (i4 & 8) != 0 ? 0 : i3);
    }

    public final String getName() {
        return this.name;
    }

    protected final int getId() {
        return this.f186id;
    }

    public final int getVariable() {
        return this.variable;
    }

    public final void setVariable(int i) {
        this.variable = i;
    }

    public final void printAge() {
        System.out.println((Object) ("Age: " + this.age));
    }

    public final void printId() {
        System.out.println((Object) ("Id: " + this.f186id));
    }
}