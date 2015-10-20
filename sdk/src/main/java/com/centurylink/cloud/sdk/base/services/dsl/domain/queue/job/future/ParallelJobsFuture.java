/*
 * (c) 2015 CenturyLink. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future;

import com.centurylink.cloud.sdk.base.services.client.QueueClient;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.JobInfo;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.exceptions.JobFailedException;
import com.centurylink.cloud.sdk.core.exceptions.ErrorsContainer;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions.allItemsNotNull;
import static com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions.notNull;
import static java.util.stream.Collectors.toList;

/**
 * @author Ilya Drabenia
 */
public class ParallelJobsFuture implements JobFuture {
    private final ErrorsContainer errors =
            new ErrorsContainer(msg -> new JobFailedException(msg));

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

        return futureList.toArray(new CompletableFuture[futureList.size()]);
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
