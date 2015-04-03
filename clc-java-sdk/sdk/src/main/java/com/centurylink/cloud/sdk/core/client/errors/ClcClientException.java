package com.centurylink.cloud.sdk.core.client.errors;

import com.centurylink.cloud.sdk.core.exceptions.ClcException;

/**
 * @author ilya.drabenia
 */
public class ClcClientException extends ClcException {

    public ClcClientException(String message) {
        super(message);
    }

    public ClcClientException(Throwable cause) {
        super(cause);
    }
}
