package com.centurylink.cloud.sdk.core.commons.services.domain.queue.future;

import com.centurylink.cloud.sdk.core.commons.client.QueueClient;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.job.ExecutingJob;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.job.CompositeExecutingJob;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.job.SimpleExecutingJob;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * @author ilya.drabenia
 */
public class OperationFuture<T> {
    private final ExecutingJob waiting;
    private final T result;

    public OperationFuture(T result, String statusId, QueueClient queueClient) {
        this(result, new SimpleExecutingJob(statusId, queueClient));
    }

    public OperationFuture(T result, List<String> statusIds, QueueClient queueClient) {
        this(
            result,
            new CompositeExecutingJob(
                checkNotNull(statusIds)
                    .stream()
                    .map(status -> new SimpleExecutingJob(status, queueClient))
                    .collect(toList())
            )
        );
    }

    public OperationFuture(T result, ExecutingJob waiting) {
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

    public static OperationFuture<List<?>> from(OperationFuture<?>... futures) {
        return new OperationFuture<>(
            Stream.of(futures).map(f -> f.getResult()).collect(toList()),
            new CompositeExecutingJob(
                Stream.of(futures).map(f -> f.waiting).collect(toList())
            )
        );
    }

}
