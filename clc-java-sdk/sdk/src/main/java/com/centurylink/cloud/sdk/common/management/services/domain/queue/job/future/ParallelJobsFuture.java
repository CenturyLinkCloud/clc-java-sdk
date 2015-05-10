package com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future;

import com.centurylink.cloud.sdk.common.management.client.QueueClient;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.JobInfo;
import com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.centurylink.cloud.sdk.core.function.Streams.map;
import static com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions.allItemsNotNull;
import static com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions.notNull;
import static com.google.common.collect.Iterables.toArray;
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

    public ParallelJobsFuture(List<JobInfo> jobInfoList, QueueClient queueClient) {
        allItemsNotNull(jobInfoList, "Job Info List");
        notNull(queueClient, "Queue Client must be not null");

        this.jobs = map(jobInfoList, info -> new SingleJobFuture(info, queueClient));
    }

    @Override
    public void waitUntilComplete() {
        jobs.forEach(JobFuture::waitUntilComplete);
    }

    @Override
    public void waitUntilComplete(Duration timeout) {
        jobs.forEach(j -> j.waitUntilComplete(timeout));
    }

    @Override
    public CompletableFuture<Void> waitAsync() {
        return CompletableFuture.allOf(toArray(
            map(jobs, JobFuture::waitAsync),
            CompletableFuture.class
        ));
    }

}
