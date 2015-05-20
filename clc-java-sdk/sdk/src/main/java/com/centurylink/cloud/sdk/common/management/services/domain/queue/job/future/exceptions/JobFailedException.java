package com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.exceptions;

import com.centurylink.cloud.sdk.core.services.ClcServiceException;

/**
 * @author Ilya Drabenia
 */
public class JobFailedException extends ClcServiceException {

    public JobFailedException() {
        super();
    }

    public JobFailedException(String message) {
        super(message);
    }

    public JobFailedException(String format, Object... arguments) {
        super(format, arguments);
    }
}
