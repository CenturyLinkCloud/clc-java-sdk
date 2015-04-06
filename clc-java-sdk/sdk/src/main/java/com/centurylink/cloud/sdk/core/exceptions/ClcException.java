package com.centurylink.cloud.sdk.core.exceptions;

import java.util.List;

import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.Iterables.toArray;
import static java.util.Arrays.asList;

/**
 * @author ilya.drabenia
 */
public class ClcException extends RuntimeException {

    public ClcException() {
    }

    public ClcException(String format, Object... arguments) {
        this(String.format(format, rejectLastThrowable(arguments)));

        if (lastItem(arguments) instanceof Throwable) {
            this.addSuppressed((Throwable) lastItem(arguments));
        }
    }

    private Object lastItem(Object[] arguments) {
        return arguments[arguments.length - 1];
    }

    private static Object[] rejectLastThrowable(Object[] args) {
        return toArray(
            rejectLastThrowable(asList(args)), Object.class
        );
    }

    private static List<?> rejectLastThrowable(List<?> args) {
        if (getLast(args) instanceof Throwable) {
            return args.subList(0, args.size() - 1);
        } else {
            return args;
        }
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
