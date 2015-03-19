package com.centurylink.cloud.sdk.core.client.errors;

import com.centurylink.cloud.sdk.core.exceptions.ClcException;

/**
 * @author ilya.drabenia
 */
public class ClcServiceException extends ClcException {

    public ClcServiceException(String message) {
        super(message);
    }

    public ClcServiceException(Throwable cause) {
        super(cause);
    }
}
