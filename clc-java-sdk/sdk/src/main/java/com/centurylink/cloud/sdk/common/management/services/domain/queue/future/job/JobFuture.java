package com.centurylink.cloud.sdk.common.management.services.domain.queue.future.job;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * @author Ilya Drabenia
 */
public interface JobFuture {

    void waitUntilComplete();

    void waitUntilComplete(Duration timeout);

    // TODO: need to think about type of first parameter
    // TODO: I think it may be response type of queue client
    CompletableFuture<Void> waitAsync();

}
