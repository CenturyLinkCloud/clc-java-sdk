package com.centurylink.cloud.sdk.servers.services.domain.remote;

import com.centurylink.cloud.sdk.core.client.ClcClientException;

/**
 * @author Anton Karavayeu
 */
public class SshException extends ClcClientException {
    public SshException(Throwable cause) {
        super(cause);
    }
}
