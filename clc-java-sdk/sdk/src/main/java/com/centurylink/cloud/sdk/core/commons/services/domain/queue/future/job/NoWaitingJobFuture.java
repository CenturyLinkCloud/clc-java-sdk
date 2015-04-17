package com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.job;

import com.centurylink.cloud.sdk.core.commons.client.QueueClient;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.ClcTimeoutException;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.OperationFailedException;
import com.google.common.base.Throwables;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

/**
 * @author Aliaksandr Krasitski
 */
public class NoWaitingJobFuture implements JobFuture {

    @Override
    public void waitUntilComplete() {
        doWaitUntilComplete(null);
    }

    @Override
    public void waitUntilComplete(Duration timeout) {
        doWaitUntilComplete(timeout);
    }

    private void doWaitUntilComplete(Duration timeout) {
    }

    @Override
    public CompletableFuture<Void> waitAsync() {
        return new CompletableFuture<Void>();
    }

}
