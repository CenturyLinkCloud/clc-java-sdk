package com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.waiting;

import java.util.function.Consumer;

/**
 * @author Ilya Drabenia
 */
public class CompositeWaitingLoop implements WaitingLoop {
    private final WaitingLoop firstLoop;
    private final WaitingLoop secondLoop;

    public CompositeWaitingLoop(WaitingLoop firstLoop, WaitingLoop secondLoop) {
        this.firstLoop = firstLoop;
        this.secondLoop = secondLoop;
    }

    @Override
    public WaitingLoop onIterationStarted(Consumer<Void> listener) {
        firstLoop.onIterationStarted(listener);
        secondLoop.onIterationStarted(listener);
        return this;
    }

    @Override
    public Void get() {
        firstLoop.get();
        secondLoop.get();

        return null;
    }
}
