package com.centurylink.cloud.sdk.core.exceptions.fails;

import com.centurylink.cloud.sdk.core.exceptions.ClcException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

/**
 * @author Ilya Drabenia
 */
public class SingleCallResult<X, R> implements CallResult<X, R> {
    private final X resource;
    private final CompletableFuture<R> result = new CompletableFuture<>();
    private final List<Exception> exceptions = new CopyOnWriteArrayList<>();

    public SingleCallResult(X resource) {
        this.resource = resource;
    }

    public SingleCallResult(X resource, R resultValue) {
        this.resource = resource;
        this.result.complete(resultValue);
    }

    public SingleCallResult<X, R> addException(Exception ex) {
        exceptions.add(ex);
        return this;
    }

    @Override
    public Stream<X> getArgument() {
        return Stream.of(resource);
    }

    @Override
    public Stream<R> getResult() {
        try {
            return Stream.of(result.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new ClcException(e);
        }
    }

    @Override
    public Stream<Exception> getExceptions() {
        return exceptions.stream();
    }

    @Override
    public CompletableFuture<Stream<R>> future() {
        return result.thenApply(Stream::of);
    }
}
