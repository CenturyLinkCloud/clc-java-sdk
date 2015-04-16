package com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.job;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Ilya Drabenia
 */
public class CompositeExecutingJob implements ExecutingJob {
    private final List<ExecutingJob> jobs;

    public CompositeExecutingJob(List<ExecutingJob> jobs) {
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
    public void waitAsync(BiConsumer<Void, ? extends Throwable> listener) {

    }

    @Override
    public void waitAsync(BiConsumer<Void, ? extends Throwable> listener, Duration timeout) {

    }

    private void doWaitUntilComplete(Duration timeout) {
        jobs
            .stream()
            .forEach(job -> job.waitUntilComplete(timeout));
    }

}
