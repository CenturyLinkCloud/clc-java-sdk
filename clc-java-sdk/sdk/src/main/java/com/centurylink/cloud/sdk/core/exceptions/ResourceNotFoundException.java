package com.centurylink.cloud.sdk.core.exceptions;

/**
 * @author ilya.drabenia
 */
public class ResourceNotFoundException extends ClcServiceException {

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

}
