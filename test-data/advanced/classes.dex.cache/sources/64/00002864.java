package kotlinx.coroutines;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.ContinuationInterceptor;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Functions;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Ref;
import kotlinx.coroutines.internal.ThreadContext;

@Metadata(m29d1 = {"\u0000>\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a \u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\nH\u0002\u001a8\u0010\u000b\u001a\u0002H\f\"\u0004\b\u0000\u0010\f2\n\u0010\r\u001a\u0006\u0012\u0002\b\u00030\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\f0\u0012H\u0080\b¢\u0006\u0002\u0010\u0013\u001a4\u0010\u0014\u001a\u0002H\f\"\u0004\b\u0000\u0010\f2\u0006\u0010\u0015\u001a\u00020\u00032\b\u0010\u000f\u001a\u0004\u0018\u00010\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\f0\u0012H\u0080\b¢\u0006\u0002\u0010\u0016\u001a\f\u0010\u0017\u001a\u00020\n*\u00020\u0003H\u0002\u001a\u0014\u0010\u0018\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u0003H\u0007\u001a\u0014\u0010\u0018\u001a\u00020\u0003*\u00020\u001a2\u0006\u0010\u0015\u001a\u00020\u0003H\u0007\u001a\u0013\u0010\u001b\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u001c*\u00020\u001dH\u0080\u0010\u001a(\u0010\u001e\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u001c*\u0006\u0012\u0002\b\u00030\u000e2\u0006\u0010\u0015\u001a\u00020\u00032\b\u0010\u001f\u001a\u0004\u0018\u00010\u0010H\u0000\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u0001*\u00020\u00038@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005¨\u0006 "}, m28d2 = {"DEBUG_THREAD_NAME_SEPARATOR", "", "coroutineName", "Lkotlin/coroutines/CoroutineContext;", "getCoroutineName", "(Lkotlin/coroutines/CoroutineContext;)Ljava/lang/String;", "foldCopies", "originalContext", "appendContext", "isNewCoroutine", "", "withContinuationContext", "T", "continuation", "Lkotlin/coroutines/Continuation;", "countOrElement", "", "block", "Lkotlin/Function0;", "(Lkotlin/coroutines/Continuation;Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "withCoroutineContext", "context", "(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "hasCopyableElements", "newCoroutineContext", "addedContext", "Lkotlinx/coroutines/CoroutineScope;", "undispatchedCompletion", "Lkotlinx/coroutines/UndispatchedCoroutine;", "Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "updateUndispatchedCompletion", "oldValue", "kotlinx-coroutines-core"}, m27k = 2, m26mv = {1, 6, 0}, m24xi = 48)
/* renamed from: kotlinx.coroutines.CoroutineContextKt */
/* loaded from: /Users/yanghyeonseo/gitprojects/kovik/test-data/advanced/classes5.dex */
public final class CoroutineContext {
    private static final String DEBUG_THREAD_NAME_SEPARATOR = " @";

    public static final kotlin.coroutines.CoroutineContext newCoroutineContext(CoroutineScope $this$newCoroutineContext, kotlin.coroutines.CoroutineContext context) {
        kotlin.coroutines.CoroutineContext combined = foldCopies($this$newCoroutineContext.getCoroutineContext(), context, true);
        kotlin.coroutines.CoroutineContext debug = Debug.getDEBUG() ? combined.plus(new CoroutineId(Debug.getCOROUTINE_ID().incrementAndGet())) : combined;
        return (combined == Dispatchers.getDefault() || combined.get(ContinuationInterceptor.Key) != null) ? debug : debug.plus(Dispatchers.getDefault());
    }

    public static final kotlin.coroutines.CoroutineContext newCoroutineContext(kotlin.coroutines.CoroutineContext $this$newCoroutineContext, kotlin.coroutines.CoroutineContext addedContext) {
        return !hasCopyableElements(addedContext) ? $this$newCoroutineContext.plus(addedContext) : foldCopies($this$newCoroutineContext, addedContext, false);
    }

    private static final boolean hasCopyableElements(kotlin.coroutines.CoroutineContext $this$hasCopyableElements) {
        return ((Boolean) $this$hasCopyableElements.fold(false, new Function2<Boolean, CoroutineContext.Element, Boolean>() { // from class: kotlinx.coroutines.CoroutineContextKt$hasCopyableElements$1
            public final Boolean invoke(boolean result, CoroutineContext.Element it) {
                return Boolean.valueOf(result || (it instanceof CopyableThreadContextElement));
            }

            @Override // kotlin.jvm.functions.Function2
            public /* bridge */ /* synthetic */ Boolean invoke(Boolean bool, CoroutineContext.Element element) {
                return invoke(bool.booleanValue(), element);
            }
        })).booleanValue();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v7, types: [T, java.lang.Object] */
    private static final kotlin.coroutines.CoroutineContext foldCopies(kotlin.coroutines.CoroutineContext originalContext, kotlin.coroutines.CoroutineContext appendContext, final boolean isNewCoroutine) {
        boolean hasElementsLeft = hasCopyableElements(originalContext);
        boolean hasElementsRight = hasCopyableElements(appendContext);
        if (!hasElementsLeft && !hasElementsRight) {
            return originalContext.plus(appendContext);
        }
        final Ref.ObjectRef leftoverContext = new Ref.ObjectRef();
        leftoverContext.element = appendContext;
        kotlin.coroutines.CoroutineContext folded = (kotlin.coroutines.CoroutineContext) originalContext.fold(EmptyCoroutineContext.INSTANCE, new Function2<kotlin.coroutines.CoroutineContext, CoroutineContext.Element, kotlin.coroutines.CoroutineContext>() { // from class: kotlinx.coroutines.CoroutineContextKt$foldCopies$folded$1
            /* JADX INFO: Access modifiers changed from: package-private */
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(2);
            }

            /* JADX WARN: Type inference failed for: r2v3, types: [T, kotlin.coroutines.CoroutineContext] */
            @Override // kotlin.jvm.functions.Function2
            public final CoroutineContext invoke(CoroutineContext result, CoroutineContext.Element element) {
                if (element instanceof CopyableThreadContextElement) {
                    CoroutineContext.Element newElement = leftoverContext.element.get(element.getKey());
                    if (newElement == null) {
                        return result.plus(isNewCoroutine ? ((CopyableThreadContextElement) element).copyForChild() : (CopyableThreadContextElement) element);
                    }
                    leftoverContext.element = leftoverContext.element.minusKey(element.getKey());
                    return result.plus(((CopyableThreadContextElement) element).mergeForChild(newElement));
                }
                return result.plus(element);
            }
        });
        if (hasElementsRight) {
            leftoverContext.element = ((kotlin.coroutines.CoroutineContext) leftoverContext.element).fold(EmptyCoroutineContext.INSTANCE, new Function2<kotlin.coroutines.CoroutineContext, CoroutineContext.Element, kotlin.coroutines.CoroutineContext>() { // from class: kotlinx.coroutines.CoroutineContextKt$foldCopies$1
                @Override // kotlin.jvm.functions.Function2
                public final CoroutineContext invoke(CoroutineContext result, CoroutineContext.Element element) {
                    if (element instanceof CopyableThreadContextElement) {
                        return result.plus(((CopyableThreadContextElement) element).copyForChild());
                    }
                    return result.plus(element);
                }
            });
        }
        return folded.plus((kotlin.coroutines.CoroutineContext) leftoverContext.element);
    }

    public static final <T> T withCoroutineContext(kotlin.coroutines.CoroutineContext context, Object countOrElement, Functions<? extends T> functions) {
        Object oldValue = ThreadContext.updateThreadContext(context, countOrElement);
        try {
            return functions.invoke();
        } finally {
            InlineMarker.finallyStart(1);
            ThreadContext.restoreThreadContext(context, oldValue);
            InlineMarker.finallyEnd(1);
        }
    }

    public static final <T> T withContinuationContext(Continuation<?> continuation, Object countOrElement, Functions<? extends T> functions) {
        UndispatchedCoroutine undispatchedCompletion;
        kotlin.coroutines.CoroutineContext context = continuation.getContext();
        Object oldValue = ThreadContext.updateThreadContext(context, countOrElement);
        if (oldValue != ThreadContext.NO_THREAD_ELEMENTS) {
            undispatchedCompletion = updateUndispatchedCompletion(continuation, context, oldValue);
        } else {
            undispatchedCompletion = null;
        }
        try {
            return functions.invoke();
        } finally {
            InlineMarker.finallyStart(1);
            if (undispatchedCompletion == null || undispatchedCompletion.clearThreadContext()) {
                ThreadContext.restoreThreadContext(context, oldValue);
            }
            InlineMarker.finallyEnd(1);
        }
    }

    public static final UndispatchedCoroutine<?> updateUndispatchedCompletion(Continuation<?> continuation, kotlin.coroutines.CoroutineContext context, Object oldValue) {
        if (continuation instanceof CoroutineStackFrame) {
            boolean potentiallyHasUndispatchedCoroutine = context.get(UndispatchedMarker.INSTANCE) != null;
            if (potentiallyHasUndispatchedCoroutine) {
                UndispatchedCoroutine completion = undispatchedCompletion((CoroutineStackFrame) continuation);
                if (completion != null) {
                    completion.saveThreadContext(context, oldValue);
                }
                return completion;
            }
            return null;
        }
        return null;
    }

    public static final UndispatchedCoroutine<?> undispatchedCompletion(CoroutineStackFrame $this$undispatchedCompletion) {
        CoroutineStackFrame completion = $this$undispatchedCompletion;
        while (!(completion instanceof DispatchedCoroutine) && (completion = completion.getCallerFrame()) != null) {
            if (completion instanceof UndispatchedCoroutine) {
                return (UndispatchedCoroutine) completion;
            }
        }
        return null;
    }

    public static final String getCoroutineName(kotlin.coroutines.CoroutineContext $this$coroutineName) {
        CoroutineId coroutineId;
        if (Debug.getDEBUG() && (coroutineId = (CoroutineId) $this$coroutineName.get(CoroutineId.Key)) != null) {
            CoroutineName coroutineName = (CoroutineName) $this$coroutineName.get(CoroutineName.Key);
            String coroutineName2 = (coroutineName == null || (coroutineName2 = coroutineName.getName()) == null) ? "coroutine" : "coroutine";
            return coroutineName2 + '#' + coroutineId.getId();
        }
        return null;
    }
}