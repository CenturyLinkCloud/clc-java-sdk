package com.centurylink.cloud.sdk.common.management.services.domain.queue.future;

import com.centurylink.cloud.sdk.core.services.ClcServiceException;

/**
 * @author Ilya Drabenia
 */
public class OperationFailedException extends ClcServiceException {

    public OperationFailedException() {
        super();
    }

    public OperationFailedException(String message) {
        super(message);
    }

    public OperationFailedException(String format, Object... arguments) {
        super(format, arguments);
    }
}
