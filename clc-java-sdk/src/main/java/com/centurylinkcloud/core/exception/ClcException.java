package com.centurylinkcloud.core.exception;

/**
 * @author ilya.drabenia
 */
public class ClcException extends RuntimeException {

    public ClcException() {
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
