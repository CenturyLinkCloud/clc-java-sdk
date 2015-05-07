package com.centurylink.cloud.sdk.common.management.services.domain.queue.future.job.waiting;

import com.google.common.base.Throwables;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.lang.Boolean.TRUE;

/**
 * @author Ilya Drabenia
 */
public class SingleWaitingLoop implements WaitingLoop {
    public static final long STATUS_POLLING_DELAY = 400L;

    private final Consumer<Void> onIterationStarted;
    private final Supplier<Boolean> checkStatus;

    public SingleWaitingLoop(Supplier<Boolean> checkStatus) {
        this(checkStatus, e -> { });
    }

    private SingleWaitingLoop(Supplier<Boolean> checkStatus, Consumer<Void> onIterationStarted) {
        this.checkStatus = checkStatus;
        this.onIterationStarted = onIterationStarted;
    }

    @Override
    public WaitingLoop onIterationStarted(Consumer<Void> listener) {
        return new SingleWaitingLoop(checkStatus, onIterationStarted.andThen(listener));
    }

    @Override
    public Void get() {
        onIterationStarted.accept(null);

        if (TRUE.equals(checkStatus.get())) {
            return null;
        }

        for (;;) {
            onIterationStarted.accept(null);

            if (TRUE.equals(checkStatus.get())) {
                waitingForBackendConsistentState();
                return null;
            }

            sleep();
        }
    }

    private void waitingForBackendConsistentState() {
        sleep(10_000L);
    }

    private void sleep() {
        sleep(STATUS_POLLING_DELAY);
    }

    private void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ex) {
            throw Throwables.propagate(ex);
        }
    }

}
