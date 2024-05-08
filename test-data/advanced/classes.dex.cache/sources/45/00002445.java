package com.example.sample;

import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: StaticExample.kt */
@Metadata(m29d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \u00052\u00020\u0001:\u0001\u0005B\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004¨\u0006\u0006"}, m28d2 = {"Lcom/example/sample/StaticExample;", "", "()V", "doTest", "", "Companion", "app_debug"}, m27k = 1, m26mv = {1, 9, 0}, m24xi = 48)
/* loaded from: /Users/yanghyeonseo/gitprojects/kovik/test-data/advanced/classes4.dex */
public final class StaticExample {
    public static final int $stable = 0;
    public static final Companion Companion = new Companion(null);
    private static int count;

    @JvmStatic
    public static final void realStaticMethod() {
        Companion.realStaticMethod();
    }

    /* compiled from: StaticExample.kt */
    @Metadata(m29d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0006\u0010\t\u001a\u00020\nJ\u0006\u0010\u000b\u001a\u00020\nJ\u000e\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\rJ\b\u0010\u000e\u001a\u00020\nH\u0007R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b¨\u0006\u000f"}, m28d2 = {"Lcom/example/sample/StaticExample$Companion;", "", "()V", "count", "", "getCount", "()I", "setCount", "(I)V", "incrementCount", "", "printMessage", "message", "", "realStaticMethod", "app_debug"}, m27k = 1, m26mv = {1, 9, 0}, m24xi = 48)
    /* loaded from: /Users/yanghyeonseo/gitprojects/kovik/test-data/advanced/classes4.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final void printMessage() {
            System.out.println((Object) "This is a static method");
        }

        public final void printMessage(String message) {
            Intrinsics.checkNotNullParameter(message, "message");
            System.out.println((Object) ("This is a static method with message: " + message));
        }

        public final int getCount() {
            return StaticExample.count;
        }

        public final void setCount(int i) {
            StaticExample.count = i;
        }

        public final void incrementCount() {
            setCount(getCount() + 1);
        }

        @JvmStatic
        public final void realStaticMethod() {
            System.out.println((Object) "This is a real static method");
            setCount(getCount() + 1);
            incrementCount();
        }
    }

    public final void doTest() {
        Companion.printMessage();
        Companion.printMessage("Hello");
        Companion.incrementCount();
        Companion.incrementCount();
        System.out.println((Object) ("Count: " + count));
        Companion.realStaticMethod();
    }
}