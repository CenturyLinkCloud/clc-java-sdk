package com.centurylink.cloud.sdk.core.exceptions;

import com.centurylink.cloud.sdk.core.client.ClcClientException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * @author Ilya Drabenia
 */
public class ErrorsContainer {
    private final List<Throwable> errors = new CopyOnWriteArrayList<>();

    public boolean hasErrors() {
        return errors.size() > 0;
    }

    public ClcClientException summaryException() {
        if (hasErrors()) {
            ClcClientException ex = new ClcClientException();
            errors.forEach(ex::addSuppressed);
            return ex;
        } else {
            return null;
        }
    }

    public <T> Consumer<T> intercept(Consumer<T> func) {
        return (T val) -> {
            try {
                func.accept(val);
            } catch (ClcException ex) {
                errors.add(ex);
            }
        };
    }

    public ErrorsContainer add(Throwable ex) {
        errors.add(ex);
        return this;
    }

}
