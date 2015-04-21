package com.centurylink.cloud.sdk.core.services;

import com.centurylink.cloud.sdk.core.services.ClcServiceException;

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

    public ResourceNotFoundException(String format, Object... arguments) {
        super(format, arguments);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

}
