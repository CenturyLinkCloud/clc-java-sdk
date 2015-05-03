package com.centurylink.cloud.sdk.base.exceptions;

import static java.lang.String.format;

/**
 * Base CenturyLink Cloud SDK Exception
 *
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
