package com.example.sample;

import kotlin.Metadata;
import kotlinx.coroutines.BuildersKt__BuildersKt;

/* compiled from: CoroutineExample.kt */
@Metadata(m29d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\bÇ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004J\u0011\u0010\u0005\u001a\u00020\u0006H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0007\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\b"}, m28d2 = {"Lcom/example/sample/CoroutineExample;", "", "()V", "doTest", "", "simulateLongRunningTask", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"}, m27k = 1, m26mv = {1, 9, 0}, m24xi = 48)
/* loaded from: /Users/yanghyeonseo/gitprojects/kovik/test-data/advanced/classes4.dex */
public final class CoroutineExample {
    public static final int $stable = 0;
    public static final CoroutineExample INSTANCE = new CoroutineExample();

    private CoroutineExample() {
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x002d  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0031  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.lang.Object simulateLongRunningTask(kotlin.coroutines.Continuation<? super java.lang.Integer> r5) {
        /*
            r4 = this;
            boolean r0 = r5 instanceof com.example.sample.CoroutineExample$simulateLongRunningTask$1
            if (r0 == 0) goto L14
            r0 = r5
            com.example.sample.CoroutineExample$simulateLongRunningTask$1 r0 = (com.example.sample.CoroutineExample$simulateLongRunningTask$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r5 = r0.label
            int r5 = r5 - r2
            r0.label = r5
            goto L19
        L14:
            com.example.sample.CoroutineExample$simulateLongRunningTask$1 r0 = new com.example.sample.CoroutineExample$simulateLongRunningTask$1
            r0.<init>(r4, r5)
        L19:
            r5 = r0
            java.lang.Object r0 = r5.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r5.label
            switch(r2) {
                case 0: goto L31;
                case 1: goto L2d;
                default: goto L25;
            }
        L25:
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
            r5.<init>(r0)
            throw r5
        L2d:
            kotlin.ResultKt.throwOnFailure(r0)
            goto L40
        L31:
            kotlin.ResultKt.throwOnFailure(r0)
            r2 = 1
            r5.label = r2
            r2 = 1000(0x3e8, double:4.94E-321)
            java.lang.Object r2 = kotlinx.coroutines.DelayKt.delay(r2, r5)
            if (r2 != r1) goto L40
            return r1
        L40:
            r1 = 42
            java.lang.Integer r1 = kotlin.coroutines.jvm.internal.boxing.boxInt(r1)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.sample.CoroutineExample.simulateLongRunningTask(kotlin.coroutines.Continuation):java.lang.Object");
    }

    public final void doTest() {
        BuildersKt__BuildersKt.runBlocking$default(null, new CoroutineExample$doTest$1(null), 1, null);
    }
}