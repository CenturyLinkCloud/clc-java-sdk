package com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.waiting;

import com.centurylink.cloud.sdk.core.commons.client.QueueClient;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.OperationFailedException;
import com.google.common.base.Throwables;

import java.time.Duration;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Ilya Drabenia
 */
public class SimpleCompleteWaiting implements CompleteWaiting {
    public static final long STATUS_POLLING_DELAY = 400L;

    private final QueueClient queueClient;
    private final String statusId;

    public SimpleCompleteWaiting(String statusId, QueueClient queueClient) {
        this.queueClient = queueClient;
        this.statusId = statusId;
    }

    @Override
    public void waitUntilComplete() {
        if (statusId == null) {
            throw new OperationFailedException();
        }

        for (;;) {
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
    public void waitUntilComplete(Duration timeout) {
        this.waitUntilComplete(); // TODO: need to implement this method properly
    }

    @Override
    public <T> void completeListener(Consumer<T> listener) {

    }

    @Override
    public CompleteWaiting and(CompleteWaiting otherStrategy) {
        return new CompositeCompleteWaiting(this, checkNotNull(otherStrategy));
    }

}
