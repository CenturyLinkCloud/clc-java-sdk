package com.centurylink.cloud.sdk.core.commons.services.domain.queue.future;

import com.centurylink.cloud.sdk.core.commons.client.QueueClient;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.waiting.CompleteWaiting;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.waiting.CompositeCompleteWaiting;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.waiting.SimpleCompleteWaiting;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * @author ilya.drabenia
 */
public class OperationFuture<T> {
    private final CompleteWaiting waiting;
    private final T result;

    public OperationFuture(T result, String statusId, QueueClient queueClient) {
        this(result, new SimpleCompleteWaiting(statusId, queueClient));
    }

    public OperationFuture(T result, List<String> statusIds, QueueClient queueClient) {
        this(
            result,
            new CompositeCompleteWaiting(
                checkNotNull(statusIds)
                    .stream()
                    .map(status -> new SimpleCompleteWaiting(status, queueClient))
                    .collect(toList())
            )
        );
    }

    public OperationFuture(T result, CompleteWaiting waiting) {
        this.waiting = waiting;
        this.result = result;
    }

    public OperationFuture<T> waitUntilComplete() {
        waiting.waitUntilComplete();
        return this;
    }

    public T getResult() {
        return result;
    }
}
