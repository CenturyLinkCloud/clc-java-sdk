package com.centurylink.cloud.sdk.common.management.services.domain.queue.future.job.waiting;

import com.centurylink.cloud.sdk.common.management.services.domain.queue.future.ClcTimeoutException;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;

import static java.time.Instant.now;

/**
 * @author Ilya Drabenia
 */
public class TimeoutInterceptor implements Consumer<Void> {
    private final Instant timeLimit;
    private final String operationDescription;

    public TimeoutInterceptor(Duration timeout, String operationDescription) {
        this.operationDescription = operationDescription;
        this.timeLimit = now().plus(timeout);
    }

    @Override
    public void accept(Void e) {
        if (timeLimit.isBefore(now())) {
            throw new ClcTimeoutException("Job for operation %s is timed out", operationDescription);
        }
    }

}
