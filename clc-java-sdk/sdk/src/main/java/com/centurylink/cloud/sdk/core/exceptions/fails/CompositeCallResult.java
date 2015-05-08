package com.centurylink.cloud.sdk.core.exceptions.fails;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

/**
 * @author Ilya Drabenia
 */
@SuppressWarnings("unchecked")
public class CompositeCallResult<T, R> implements CallResult<T, R> {
    private final CallResult<T, R> firstResult;
    private final CallResult<T, R> secondResult;

    public CompositeCallResult(CallResult<T, R> firstResult, CallResult<T, R> secondResult) {
        this.firstResult = firstResult;
        this.secondResult = secondResult;
    }

    @Override
    public Stream<T> getArgument() {
        return concat(
            firstResult.getArgument(),
            secondResult.getArgument()
        );
    }

    @Override
    public Stream<R> getResult() {
        return concat(
            firstResult.getResult(),
            secondResult.getResult()
        );
    }

    @Override
    public Stream<Exception> getExceptions() {
        return concat(
            firstResult.getExceptions(),
            secondResult.getExceptions()
        );
    }

    @Override
    public CompletableFuture<Stream<R>> future() {
        CompletableFuture<Stream<R>> future = new CompletableFuture<>();

        CompletableFuture
            .allOf(firstResult.future(), secondResult.future())
            .thenRun(() -> future.complete(Stream.concat(
                firstResult.getResult(),
                secondResult.getResult()
            )));

        return future;
    }
}
