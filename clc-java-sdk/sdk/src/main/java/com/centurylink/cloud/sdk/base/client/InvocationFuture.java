package com.centurylink.cloud.sdk.base.client;

import com.google.common.util.concurrent.SettableFuture;

import javax.ws.rs.client.InvocationCallback;

/**
 * @author ilya.drabenia
 */
public class InvocationFuture<T> implements InvocationCallback<T> {
    private final SettableFuture<T> future = SettableFuture.create();

    @Override
    public void completed(T t) {
        future.set(t);
    }

    @Override
    public void failed(Throwable throwable) {
        future.setException(throwable);
    }

    public SettableFuture<T> toListenableFuture() {
        return future;
    }
}
