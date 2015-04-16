package com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.job;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
