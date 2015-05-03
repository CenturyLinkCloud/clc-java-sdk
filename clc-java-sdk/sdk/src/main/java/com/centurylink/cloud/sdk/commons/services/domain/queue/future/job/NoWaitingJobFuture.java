package com.centurylink.cloud.sdk.commons.services.domain.queue.future.job;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * @author Aliaksandr Krasitski
 */
public class NoWaitingJobFuture implements JobFuture {

    @Override
    public void waitUntilComplete() { }

    @Override
    public void waitUntilComplete(Duration timeout) { }

    @Override
    public CompletableFuture<Void> waitAsync() {
        return
            new CompletableFuture<Void>() {{
                complete(null);
            }};
    }

}
