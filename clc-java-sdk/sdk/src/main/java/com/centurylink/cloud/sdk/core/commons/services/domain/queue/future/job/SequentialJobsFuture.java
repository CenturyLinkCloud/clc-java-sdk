package com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.job;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static java.util.Arrays.asList;

/**
 * @author Ilya Drabenia
 */
public class SequentialJobsFuture implements JobFuture {
    private final CompletableFuture<Void> future = null;

    @SafeVarargs
    public SequentialJobsFuture(Supplier<JobFuture>... jobs) {
        List<Supplier<JobFuture>> jobList = asList(jobs);

//        future = jobList.stream()
//            .reduce(jobList.get(0).get(), (CompletableFuture<Void> prev, Supplier<JobFuture> curItem) ->
//                prev.thenApply(i -> {
//                    try {
//                        return curItem.get().waitAsync().get();
//                    } catch (InterruptedException | ExecutionException e) {
//                        throw new ClcServiceException(e);
//                    }
//                })
//            );
    }

    @Override
    public void waitUntilComplete() {
    }

    @Override
    public void waitUntilComplete(Duration timeout) {

    }

    @Override
    public CompletableFuture<Void> waitAsync() {
        return null;
    }

}
