package com.centurylink.cloud.sdk.core.exceptions;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions.notNull;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.codec.CharEncoding.UTF_8;

/**
 * @author Ilya Drabenia
 */
public class ErrorsContainer {
    private final List<Exception> errors = new CopyOnWriteArrayList<>();
    private final Function<String, ClcException> exceptionSupplier;

    public ErrorsContainer(Function<String, ClcException> exceptionSupplier) {
        notNull(exceptionSupplier, "Exception supplier must be not null");

        this.exceptionSupplier = exceptionSupplier;
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }

    private String getStackTrace(Exception ex) {
        try {
            ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
            PrintStream out = new PrintStream(outBytes, true, UTF_8);

            ex.printStackTrace(out);

            return outBytes.toString(UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new ClcException(e);
        }
    }

    public ClcException summaryException() {
        if (hasErrors()) {
            ClcException ex = exceptionSupplier.apply(errorMessage());
            errors.forEach(ex::addSubException);
            return ex;
        } else {
            return null;
        }
    }

    private String errorMessage() {
        return "\n" +
            errors
                .stream()
                .map(this::getStackTrace)
                .collect(joining("\n"));
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

    public ErrorsContainer add(Exception ex) {
        errors.add(ex);
        return this;
    }

}
