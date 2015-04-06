package com.centurylink.cloud.sdk.core.exceptions;

import java.util.List;

import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.Iterables.toArray;
import static java.lang.String.format;
import static java.util.Arrays.asList;

/**
 * @author ilya.drabenia
 */
public class ClcException extends RuntimeException {

    public ClcException() {
    }

    public ClcException(String format, Object... arguments) {
        this(format(format, arguments));

        if (lastItem(arguments) instanceof Throwable) {
            this.initCause((Throwable) lastItem(arguments));
        }
    }

    private Object lastItem(Object[] arguments) {
        return arguments[arguments.length - 1];
    }

    public ClcException(String message) {
        super(message);
    }

    public ClcException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClcException(Throwable cause) {
        super(cause);
    }

}
