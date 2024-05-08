package com.example.sample;

import java.io.File;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ObjectExample.kt */
@Metadata(m29d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0011\u0010\u000b\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u0001H\u0096\u0002J\b\u0010\r\u001a\u00020\u000eH\u0016J\b\u0010\u000f\u001a\u00020\u0003H\u0016R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\u0005X\u0086D¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\b¨\u0006\u0010"}, m28d2 = {"Lcom/example/sample/MockedSuperTest;", "Ljava/io/File;", "path", "", "myProp", "", "(Ljava/lang/String;I)V", "getMyProp", "()I", "myProp2", "getMyProp2", "compareTo", "other", "exists", "", "toString", "app_debug"}, m27k = 1, m26mv = {1, 9, 0}, m24xi = 48)
/* loaded from: /Users/yanghyeonseo/gitprojects/kovik/test-data/advanced/classes4.dex */
public final class MockedSuperTest extends File {
    public static final int $stable = 0;
    private final int myProp;
    private final int myProp2;

    public final int getMyProp() {
        return this.myProp;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public MockedSuperTest(String path, int myProp) {
        super(path);
        Intrinsics.checkNotNullParameter(path, "path");
        this.myProp = myProp;
        this.myProp2 = 5;
    }

    @Override // java.io.File
    public boolean exists() {
        return true;
    }

    public final int getMyProp2() {
        return this.myProp2;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(File other) {
        Intrinsics.checkNotNullParameter(other, "other");
        return this.myProp2;
    }

    @Override // java.io.File
    public String toString() {
        String superx = super.toString();
        Intrinsics.checkNotNullExpressionValue(superx, "toString(...)");
        return superx + ", " + this.myProp;
    }
}