package com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.waiting;

import java.time.Duration;
import java.util.function.Consumer;

/**
 * @author Ilya Drabenia
 */
public interface ExecutingJob {

    void waitUntilComplete();

    void waitUntilComplete(Duration timeout);

    <T> void completeListener(Consumer<T> listener);

}
