package com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.waiting;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Arrays.asList;

/**
 * @author Ilya Drabenia
 */
public class CompositeCompleteWaiting implements CompleteWaiting {
    private final List<CompleteWaiting> waitings;

    public CompositeCompleteWaiting() {
        waitings = new ArrayList<>();
    }

    public CompositeCompleteWaiting(CompleteWaiting... waitings) {
        this.waitings = asList(waitings);
    }

    public CompositeCompleteWaiting(List<CompleteWaiting> waitings) {
        this.waitings = new ArrayList<>(waitings);
    }

    @Override
    public void waitUntilComplete() {
        waitings
            .stream()
            .forEach(CompleteWaiting::waitUntilComplete);
    }

    @Override
    public void waitUntilComplete(Duration timeout) {
        waitUntilComplete(); // TODO: implement this method properly
    }

    @Override
    public <T> void completeListener(Consumer<T> listener) {

    }

    @Override
    public CompleteWaiting and(final CompleteWaiting otherWaiting) {
        return new CompositeCompleteWaiting(new ArrayList<CompleteWaiting>() {{
            addAll(waitings);
            add(otherWaiting);
        }});
    }

}
