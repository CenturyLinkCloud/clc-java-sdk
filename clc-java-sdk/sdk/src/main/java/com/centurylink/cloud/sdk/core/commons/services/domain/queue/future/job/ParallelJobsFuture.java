package com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.job;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static java.util.Arrays.asList;

/**
 * @author Ilya Drabenia
 */
public class ParallelJobsFuture implements JobFuture {
    private final List<JobFuture> jobs;

    public ParallelJobsFuture(JobFuture... jobs) {
        this.jobs = asList(jobs);
    }

    public ParallelJobsFuture(List<JobFuture> jobs) {
        this.jobs = new ArrayList<>(jobs);
    }

    @Override
    public void waitUntilComplete() {
        doWaitUntilComplete(null);
    }

    @Override
    public void waitUntilComplete(Duration timeout) {
        doWaitUntilComplete(timeout);
    }

    @Override
    public CompletableFuture<Void> waitAsync() {
        return null;
    }

    private void doWaitUntilComplete(Duration timeout) {
        jobs
            .stream()
            .forEach(job -> job.waitUntilComplete(timeout));
    }

}
