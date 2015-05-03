package com.centurylink.cloud.sdk.base.services;

import com.centurylink.cloud.sdk.base.exceptions.ClcException;

/**
 * Base class for all service layer exceptions
 *
 * @author Ilya Drabenia
 */
public class ClcServiceException extends ClcException {

    public ClcServiceException() {
    }

    public ClcServiceException(String format, Object... arguments) {
        super(format, arguments);
    }

    public ClcServiceException(String message) {
        super(message);
    }

    public ClcServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClcServiceException(Throwable cause) {
        super(cause);
    }

}
