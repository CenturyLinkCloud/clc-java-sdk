package com.centurylink.cloud.sdk.common.management.services.domain.queue.job;

import com.centurylink.cloud.sdk.core.ToStringMixin;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Ilya Drabenia
 */
public class JobInfo implements ToStringMixin {
    private final String statusId;

    public JobInfo(String statusId) {
        this.statusId = checkNotNull(statusId, "Status ID must be not a null");
    }

    public String getStatusId() {
        return statusId;
    }

    @Override
    public String toString() {
        return this.toReadableString();
    }
}
