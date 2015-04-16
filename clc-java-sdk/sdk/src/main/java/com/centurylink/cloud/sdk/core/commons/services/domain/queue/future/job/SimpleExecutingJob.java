package com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.job;

import com.centurylink.cloud.sdk.core.commons.client.QueueClient;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.OperationFailedException;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.OperationTimeoutException;
import com.google.common.base.Throwables;

import java.time.Duration;
import java.util.function.BiConsumer;
import java.time.Instant;

import java.util.function.Consumer;

/**
 * @author Ilya Drabenia
 */
public class SimpleExecutingJob implements ExecutingJob {
    public static final long STATUS_POLLING_DELAY = 400L;

    private final QueueClient queueClient;
    private final String statusId;

    public SimpleExecutingJob(String statusId, QueueClient queueClient) {
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
                throw new OperationTimeoutException();
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
    public void waitAsync(BiConsumer<Void, ? extends Throwable> listener) {

    }

    @Override
    public void waitAsync(BiConsumer<Void, ? extends Throwable> listener, Duration timeout) {

    }
}
