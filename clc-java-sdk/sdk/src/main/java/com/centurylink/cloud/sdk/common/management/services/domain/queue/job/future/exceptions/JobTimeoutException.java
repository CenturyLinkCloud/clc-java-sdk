package com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.exceptions;

import com.centurylink.cloud.sdk.core.services.ClcServiceException;

public class JobTimeoutException extends ClcServiceException {

    public JobTimeoutException() {
    }

    public JobTimeoutException(String format, Object... arguments) {
        super(format, arguments);
    }

    public JobTimeoutException(Throwable cause) {
        super(cause);
    }

}
