package com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.job;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static com.centurylink.cloud.sdk.core.services.predicates.Predicates.notNull;

/**
 * @author Ilya Drabenia
 */
public class SequentialJobsFuture implements JobFuture {
    private final List<Supplier<JobFuture>> jobs;

    @SafeVarargs
    public SequentialJobsFuture(Supplier<JobFuture>... jobs) {
        this.jobs = Arrays.asList(jobs);
    }

    @Override
    public void waitUntilComplete() {
        jobs
            .stream()
            .filter(notNull())
            .map(Supplier::get)
            .forEach(JobFuture::waitUntilComplete);
    }

    @Override
    public void waitUntilComplete(Duration timeout) {

    }

    @Override
    public void waitAsync(BiConsumer<Void, ? extends Throwable> listener) {

    }

    @Override
    public void waitAsync(BiConsumer<Void, ? extends Throwable> listener, Duration timeout) {

    }
}
