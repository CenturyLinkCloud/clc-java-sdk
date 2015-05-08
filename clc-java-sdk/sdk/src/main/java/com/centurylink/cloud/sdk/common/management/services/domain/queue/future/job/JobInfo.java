package com.centurylink.cloud.sdk.common.management.services.domain.queue.future.job;

import com.centurylink.cloud.sdk.core.exceptions.fails.CallResult;

/**
 * @author Ilya Drabenia
 */
public class JobInfo<T> {
    private final CallResult<T, T> call;
    private final String statusId;

    public JobInfo(CallResult<T, T> resource, String statusId) {
        this.call = resource;
        this.statusId = statusId;
    }

    public CallResult<T, T> getResource() {
        return call;
    }

    public String getStatusId() {
        return statusId;
    }
}
