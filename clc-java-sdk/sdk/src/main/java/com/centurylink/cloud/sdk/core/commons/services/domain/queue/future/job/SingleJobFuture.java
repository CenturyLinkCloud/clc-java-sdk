package com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.job;

import com.centurylink.cloud.sdk.core.commons.client.QueueClient;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.OperationFailedException;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.ClcTimeoutException;
import com.centurylink.cloud.sdk.core.services.SdkThreadPool;
import com.google.common.base.Throwables;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

/**
 * @author Ilya Drabenia
 */
public class SingleJobFuture implements JobFuture {
    public static final long STATUS_POLLING_DELAY = 400L;

    private final QueueClient queueClient;
    private final String statusId;

    public SingleJobFuture(String statusId, QueueClient queueClient) {
        this.queueClient = queueClient;
        this.statusId = statusId;
    }

    @Override
    public void waitUntilComplete() {
        doWaitUntilComplete(null);
    }

    @Override
    public void waitUntilComplete(Duration timeout) {
        doWaitUntilComplete(timeout);
    }

    private void doWaitUntilComplete(Duration timeout) {
        if (statusId == null) {
            throw new OperationFailedException();
        }

        Instant timeLimit = null;

        if (timeout != null) {
            timeLimit = Instant.now().plusSeconds(timeout.getSeconds());
        }

        for (;;) {
            if (timeLimit != null && timeLimit.isBefore(Instant.now())) {
                throw new ClcTimeoutException();
            }

            String status = queueClient
                    .getJobStatus(statusId)
                    .getStatus();

            switch (status) {
                case "succeeded":
                    return;

                case "failed":
                case "unknown":
                    throw new OperationFailedException();

                default:
                    try {
                        Thread.sleep(STATUS_POLLING_DELAY);
                    } catch (InterruptedException ex) {
                        throw Throwables.propagate(ex);
                    }
            }
        }
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

}
