package com.centurylink.cloud.sdk.core.exceptions;

import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.String.format;

/**
 * Base CenturyLink Cloud SDK Exception
 *
 * @author ilya.drabenia
 */
public class ClcException extends RuntimeException {
    private final CopyOnWriteArrayList<Exception> subExceptions
        = new CopyOnWriteArrayList<>();

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

    public CopyOnWriteArrayList<Exception> getSubExceptions() {
        return subExceptions;
    }

    public void addSubException(Exception ex) {
        subExceptions.add(ex);
    }
}
