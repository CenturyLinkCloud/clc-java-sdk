package com.centurylink.cloud.sdk.common.management.services.domain.queue.future;

import com.centurylink.cloud.sdk.common.management.client.QueueClient;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.future.job.*;
import com.centurylink.cloud.sdk.core.exceptions.fails.CallResult;
import com.centurylink.cloud.sdk.core.exceptions.fails.SingleCallResult;
import com.centurylink.cloud.sdk.core.services.ClcServiceException;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.function.Streams.map;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * @author ilya.drabenia
 */
public class OperationFuture<T> {
    private final JobFuture waiting;
    private final CallResult<T, T> result;

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

    public OperationFuture(List<JobInfo<T>> result, QueueClient queueClient) {
        this.result =
            result.stream()
                .map(JobInfo::getResource)
                .map(j -> (CallResult<T, T>) new SingleCallResult<>(j))
                .reduce(CallResult::compose)
                .get();

        this.waiting =
            new ParallelJobsFuture(
                result.stream()
                    .map(status -> new SingleJobFuture(status, queueClient))
                    .collect(toList())
            );
    }

    public OperationFuture(T result, JobFuture waiting) {
        this.waiting = waiting;
        this.result = new SingleCallResult<>(result, result);
    }

    public OperationFuture(CallResult<T, T> result, JobFuture waiting) {
        this.waiting = waiting;
        this.result = result;
    }

    public OperationFuture<T> waitUntilComplete() {
        waiting.waitUntilComplete();
        throwExceptionIfNeeded();

        return this;
    }

    private void throwExceptionIfNeeded() {
        if (result.getExceptions().count() > 0) {
            ClcServiceException ex = new ClcServiceException();
            result.getExceptions().forEach(ex::addSuppressed);
            throw ex;
        }
    }

    public OperationFuture<T> waitUntilComplete(Duration timeout) {
        waiting.waitUntilComplete(timeout);
        throwExceptionIfNeeded();
        return this;
    }

    public T getResult() {
        return result.getResult().findFirst().get();
    }

    public Stream<T> getResultAsStream() {
        return result.getResult();
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
    public static <T> OperationFuture<T> waitUntilComplete(OperationFuture<T>... operations) {
        checkArgument(operations.length >= 1);

        return
            new OperationFuture<>(
                Stream.of(operations)
                    .map(i -> i.result)
                    .reduce(CallResult::compose)
                    .get(),

                new ParallelJobsFuture(
                    Stream.of(operations)
                        .map(i -> i.waiting)
                        .collect(toList())
                )
            );
    }

    public JobFuture jobFuture() {
        return waiting;
    }

}
