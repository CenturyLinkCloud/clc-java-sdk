package com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.job;

import com.centurylink.cloud.sdk.core.services.ClcServiceException;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import static java.util.Arrays.asList;

/**
 * @author Ilya Drabenia
 */
public class SequentialJobsFuture implements JobFuture {
    private final CompletableFuture<Void> future;

    @SafeVarargs
    public SequentialJobsFuture(Supplier<JobFuture>... jobs) {
        List<Supplier<JobFuture>> jobList = asList(jobs);

        future = jobList.stream()
            .reduce(
                jobList.get(0).get().waitAsync(),
                (prev, curItem) -> prev.thenCompose(i -> curItem.get().waitAsync()),
                (item, nextItem) -> item.thenCompose(i -> nextItem)
            );
    }

    @Override
    public void waitUntilComplete() {
        future.join();
    }

    @Override
    public void waitUntilComplete(Duration timeout) {
        try {
            future.get(timeout.get(ChronoUnit.NANOS), TimeUnit.NANOSECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new ClcServiceException(e);
        }
    }

    @Override
    public CompletableFuture<Void> waitAsync() {
        return future;
    }

}
