package com.example.sample;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Deferred;

/* compiled from: CoroutineExample.kt */
@Metadata(m29d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m28d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m27k = 3, m26mv = {1, 9, 0}, m24xi = 48)
@DebugMetadata(m14c = "com.example.sample.CoroutineExample$doTest$1", m13f = "CoroutineExample.kt", m12i = {}, m11l = {15}, m10m = "invokeSuspend", m9n = {}, m8s = {})
/* loaded from: /Users/yanghyeonseo/gitprojects/kovik/test-data/advanced/classes4.dex */
final class CoroutineExample$doTest$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    private /* synthetic */ Object L$0;
    int label;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CoroutineExample$doTest$1(Continuation<? super CoroutineExample$doTest$1> continuation) {
        super(2, continuation);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        CoroutineExample$doTest$1 coroutineExample$doTest$1 = new CoroutineExample$doTest$1(continuation);
        coroutineExample$doTest$1.L$0 = obj;
        return coroutineExample$doTest$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((CoroutineExample$doTest$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) {
        Deferred result;
        StringBuilder append;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                CoroutineScope $this$runBlocking = (CoroutineScope) this.L$0;
                result = BuildersKt__Builders_commonKt.async$default($this$runBlocking, null, null, new CoroutineExample$doTest$1$result$1(null), 3, null);
                append = new StringBuilder().append("Coroutine result: ");
                this.L$0 = append;
                this.label = 1;
                Object await = result.await(this);
                if (await != coroutine_suspended) {
                    $result = await;
                    break;
                } else {
                    return coroutine_suspended;
                }
            case 1:
                ResultKt.throwOnFailure($result);
                append = (StringBuilder) this.L$0;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        System.out.println((Object) append.append(((Number) $result).intValue()).toString());
        return Unit.INSTANCE;
    }
}