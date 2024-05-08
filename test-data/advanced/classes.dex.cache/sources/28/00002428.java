package com.example.sample;

import androidx.autofill.HintConstants;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(m29d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0017\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005¢\u0006\u0002\u0010\u0007J\u0006\u0010\f\u001a\u00020\rJ\u0006\u0010\u000e\u001a\u00020\rR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\u00020\u0005X\u0084\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u000f"}, m28d2 = {"Lcom/example/sample/AccessTest;", "", HintConstants.AUTOFILL_HINT_NAME, "", "age", "", "id", "(Ljava/lang/String;II)V", "getId", "()I", "getName", "()Ljava/lang/String;", "printAge", "", "printId", "app_debug"}, m27k = 1, m26mv = {1, 9, 0}, m24xi = 48)
/* renamed from: com.example.sample.AccessTest */
/* loaded from: /Users/yanghyeonseo/gitprojects/kovik/test-data/advanced/classes4.dex */
public class ObjectExample {
    public static final int $stable = 0;
    private final int age;

    /* renamed from: id */
    private final int f186id;
    private final String name;

    public ObjectExample(String name, int age, int id) {
        Intrinsics.checkNotNullParameter(name, "name");
        this.name = name;
        this.age = age;
        this.f186id = id;
    }

    public final String getName() {
        return this.name;
    }

    protected final int getId() {
        return this.f186id;
    }

    public final void printAge() {
        System.out.println((Object) ("Age: " + this.age));
    }

    public final void printId() {
        System.out.println((Object) ("Id: " + this.f186id));
    }
}