package com.centurylink.cloud.sdk.common.management.services.domain.queue.future;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

/**
 * @author Ilya Drabenia
 */
public class OperationResult<X> {
    private final X ref;
    private final List<Exception> exceptions = new CopyOnWriteArrayList<>();

    public OperationResult(X ref) {
        this.ref = ref;
    }

    public static <A, X> Function<A, OperationResult<X>> catchErrors(Function<A, X> func) {
        return arg -> {
            try {
                return
                    new OperationResult<>(func.apply(arg));
            } catch (Exception ex) {
                return
                    new OperationResult<X>(null)
                        .addException(ex);
            }
        };
    }

    public OperationResult<X> addException(Exception ex) {
        exceptions.add(ex);
        return this;
    }

    public X getRef() {
        return ref;
    }
}
