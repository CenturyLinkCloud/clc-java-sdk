package com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future;

import com.centurylink.cloud.sdk.common.management.client.QueueClient;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.JobInfo;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.exceptions.JobFailedException;
import com.centurylink.cloud.sdk.core.exceptions.ErrorsContainer;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.function.Streams.map;
import static com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions.allItemsNotNull;
import static com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions.notNull;
import static com.google.common.collect.Iterables.toArray;
import static java.util.stream.Collectors.toList;

/**
 * @author Ilya Drabenia
 */
public class ParallelJobsFuture implements JobFuture {
    private final ErrorsContainer errors =
            new ErrorsContainer((msg) -> new JobFailedException(msg));

    private final Stream<JobFuture> jobs;

    public ParallelJobsFuture(JobFuture... jobs) {
        this.jobs = Stream.of(jobs);
    }

    public ParallelJobsFuture(List<JobFuture> jobs) {
        this.jobs = jobs.stream();
    }

    public ParallelJobsFuture(List<JobInfo> jobInfoList, QueueClient queueClient) {
        allItemsNotNull(jobInfoList, "Job Info List");
        notNull(queueClient, "Queue Client must be not null");

        this.jobs =
            jobInfoList
                .stream()
                .map(info -> new SingleJobFuture(info, queueClient));
    }

    @Override
    public void waitUntilComplete() {
        forEachFuture(JobFuture::waitUntilComplete);
    }

    @Override
    public void waitUntilComplete(Duration timeout) {
        forEachFuture(j -> j.waitUntilComplete(timeout));
    }

    private void forEachFuture(Consumer<JobFuture> func) {
        jobs.forEach(errors.intercept(func));
        errors.throwSummaryExceptionIfNeeded();
    }

    @Override
    public CompletableFuture<Void> waitAsync() {
        Stream<CompletableFuture> futures =
            jobs
                .map(JobFuture::waitAsync)
                .map(curFuture -> curFuture.exceptionally(this::collectErrors));

        return
            CompletableFuture
                .allOf(array(futures))
                .thenApply(this::throwSummaryExceptionIfNeeded);
    }

    private CompletableFuture[] array(Stream<CompletableFuture> futures) {
        List<CompletableFuture> futureList = futures.collect(toList());

        return toArray(futureList, CompletableFuture.class);
    }

    private Void throwSummaryExceptionIfNeeded(Void val) {
        errors.throwSummaryExceptionIfNeeded();
        return null;
    }

    private Void collectErrors(Throwable ex) {
        errors.add((Exception) ex);
        return null;
    }

}
