package com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.waiting;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Ilya Drabenia
 */
public interface WaitingLoop extends Supplier<Void> {

    WaitingLoop onIterationStarted(Consumer<Void> listener);

    default WaitingLoop andThen(WaitingLoop otherLoop) {
        return new CompositeWaitingLoop(this, otherLoop);
    }

}
