package com.centurylink.cloud.sdk.core.exceptions.fails;

import com.centurylink.cloud.sdk.core.CastMixin;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * @author Ilya Drabenia
 */
public interface CallResult<T, R> extends CastMixin {

    Stream<T> getArgument();

    Stream<R> getResult();

    Stream<Exception> getExceptions();

    default CallResult<T, R> compose(CallResult<T, R> otherCallResult) {
        return new CompositeCallResult<>(this, otherCallResult);
    }

    CompletableFuture<Stream<R>> future();

}
