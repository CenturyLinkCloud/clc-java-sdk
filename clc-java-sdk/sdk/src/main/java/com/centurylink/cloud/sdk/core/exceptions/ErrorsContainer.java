package com.centurylink.cloud.sdk.core.exceptions;

import com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions;
import com.google.common.reflect.TypeToken;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions.notNull;

/**
 * @author Ilya Drabenia
 */
public class ErrorsContainer {
    private final List<Throwable> errors = new CopyOnWriteArrayList<>();
    private final Supplier<ClcException> exceptionSupplier;

    public ErrorsContainer(Supplier<ClcException> exceptionSupplier) {
        notNull(exceptionSupplier, "Exception supplier must be not null");

        this.exceptionSupplier = exceptionSupplier;
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }

    public ClcException summaryException() {
        if (hasErrors()) {
            ClcException ex = exceptionSupplier.get();
            errors.forEach(ex::addSuppressed);
            return ex;
        } else {
            return null;
        }
    }

    public void throwSummaryExceptionIfNeeded() {
        if (hasErrors()) {
            throw summaryException();
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
