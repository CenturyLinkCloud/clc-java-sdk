package com.centurylink.cloud.sdk.core.services.refs;

import com.centurylink.cloud.sdk.core.services.ClcServiceException;

/**
 * @author ilya.drabenia
 */
public class ReferenceNotResolvedException extends ClcServiceException {

    public ReferenceNotResolvedException() {
    }

    public ReferenceNotResolvedException(String message) {
        super(message);
    }

    public ReferenceNotResolvedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReferenceNotResolvedException(String format, Object... arguments) {
        super(format, arguments);
    }

    public ReferenceNotResolvedException(Throwable cause) {
        super(cause);
    }

}
