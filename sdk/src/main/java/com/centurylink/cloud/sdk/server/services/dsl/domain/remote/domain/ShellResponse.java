package com.centurylink.cloud.sdk.server.services.dsl.domain.remote.domain;

import com.centurylink.cloud.sdk.core.ToStringMixin;
import com.centurylink.cloud.sdk.core.preconditions.Preconditions;

/**
 * @author Anton Karavayeu
 */
public class ShellResponse implements ToStringMixin {
    private int errorStatus;
    private String trace;

    public ShellResponse(int errorStatus, String message) {
        this.errorStatus = Preconditions.checkNotNull(errorStatus);
        this.trace = Preconditions.checkNotNull(message);
    }

    public String getTrace() {
        return trace;
    }

    public int getErrorStatus() {
        return errorStatus;
    }

    @Override
    public String toString() {
        return toReadableString();
    }

}
