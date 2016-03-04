package com.centurylink.cloud.sdk.server.services.dsl.domain.remote;

import com.centurylink.cloud.sdk.core.client.ClcClientException;

/**
 * @author Anton Karavayeu
 */
public class SshException extends ClcClientException {
    public SshException(Throwable cause) {
        super(cause);
    }

    public SshException(String format, Object... arguments) {
        super(format, arguments);
    }
}
