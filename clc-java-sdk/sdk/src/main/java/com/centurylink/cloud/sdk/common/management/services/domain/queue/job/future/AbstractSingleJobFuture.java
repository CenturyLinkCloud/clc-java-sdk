package com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future;

import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.waiting.TimeoutInterceptor;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.waiting.WaitingLoop;
import com.centurylink.cloud.sdk.core.services.SdkThreadPool;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * @author Ilya Drabenia
 */
public abstract class AbstractSingleJobFuture implements JobFuture {

    @Override
    public void waitUntilComplete() {
        waitingLoop().get();
    }

    @Override
    public void waitUntilComplete(Duration timeout) {
        waitingLoop()
            .onIterationStarted(
                new TimeoutInterceptor(timeout, operationInfo())
            )
            .get();
    }

    @Override
    public CompletableFuture<Void> waitAsync() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        SdkThreadPool.get().execute(() -> {
            try {
                waitUntilComplete();
                future.complete(null);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    protected abstract String operationInfo();

    public abstract WaitingLoop waitingLoop();
}
