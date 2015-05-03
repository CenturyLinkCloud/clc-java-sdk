package com.centurylink.cloud.sdk.common.management.services.domain.queue.future;

import com.centurylink.cloud.sdk.core.services.ClcServiceException;

public class ClcTimeoutException extends ClcServiceException {

    public ClcTimeoutException() {
    }

    public ClcTimeoutException(String format, Object... arguments) {
        super(format, arguments);
    }

    public ClcTimeoutException(Throwable cause) {
        super(cause);
    }

}
