package com.example.sample;

import kotlin.Metadata;

/* compiled from: ThreadExample.kt */
@Metadata(m29d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004¨\u0006\u0005"}, m28d2 = {"Lcom/example/sample/ThreadExample;", "", "()V", "doTest", "", "app_debug"}, m27k = 1, m26mv = {1, 9, 0}, m24xi = 48)
/* loaded from: /Users/yanghyeonseo/gitprojects/kovik/test-data/advanced/classes4.dex */
public final class ThreadExample {
    public static final int $stable = 0;

    public final void doTest() {
        Thread thread = new Thread(new RunnableExample());
        thread.start();
        thread.join();
    }
}