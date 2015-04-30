package com.centurylink.cloud.sdk.core.commons.services.domain.queue.future;

import com.centurylink.cloud.sdk.core.commons.client.QueueClient;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.job.JobFuture;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.job.NoWaitingJobFuture;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.job.ParallelJobsFuture;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.job.SingleJobFuture;
import com.centurylink.cloud.sdk.core.services.function.Streams;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.services.function.Streams.map;
import static com.google.common.base.Preconditions.*;
import static java.util.stream.Collectors.toList;

/**
 * @author ilya.drabenia
 */
public class OperationFuture<T> {
    private final JobFuture waiting;
    private final T result;

    public OperationFuture(T result, String statusId, QueueClient queueClient) {
        this(result, new SingleJobFuture(statusId, queueClient));
    }

    public OperationFuture(T result, List<String> statusIds, QueueClient queueClient) {
        this(
            result,
            new ParallelJobsFuture(
                checkNotNull(statusIds)
                    .stream()
                    .map(status -> new SingleJobFuture(status, queueClient))
                    .collect(toList())
            )
        );
    }

    public OperationFuture(T result, JobFuture waiting) {
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

    public CompletableFuture<T> waitAsync() {
        return waiting.waitAsync().thenApply(i -> getResult());
    }

    private CompletableFuture<T> future(Consumer<BiConsumer<Void, ? extends Throwable>> listener) {
        CompletableFuture<T> future = new CompletableFuture<>();

        listener.accept((result, error) -> {
            if (error == null) {
                future.complete(getResult());
            } else {
                future.completeExceptionally(error);
            }
        });

        return future;
    }

    @SafeVarargs
    public static <T> OperationFuture<List<T>> waitUntilComplete(OperationFuture<T>... operations) {
        checkArgument(operations.length >= 1);

        return
            new OperationFuture<>(
                map(operations, op -> op.waitUntilComplete().getResult()),
                new NoWaitingJobFuture()
            );
    }

    public JobFuture jobFuture() {
        return waiting;
    }

}
