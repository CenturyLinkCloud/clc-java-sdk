package com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.waiting;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
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
        jobs
            .stream()
            .forEach(ExecutingJob::waitUntilComplete);
    }

    @Override
    public void waitUntilComplete(Duration timeout) {
        waitUntilComplete(); // TODO: implement this method properly
    }

    @Override
    public <T> void completeListener(Consumer<T> listener) {

    }

}
