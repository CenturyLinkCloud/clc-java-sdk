package com.centurylink.cloud.sdk.core.commons.services.domain.queue.future;

import com.centurylink.cloud.sdk.core.services.ClcServiceException;

public class ClcTimeoutException extends ClcServiceException {

    public ClcTimeoutException() {
    }

    public ClcTimeoutException(Throwable cause) {
        super(cause);
    }

}
