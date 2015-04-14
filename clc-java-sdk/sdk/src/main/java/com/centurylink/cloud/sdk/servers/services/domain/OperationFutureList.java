package com.centurylink.cloud.sdk.servers.services.domain;

import com.centurylink.cloud.sdk.core.exceptions.ClcException;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.google.common.base.Throwables;

import java.util.List;

public class OperationFutureList<T> {

    public static final long STATUS_POLLING_DELAY = 400L;

    private final ServerClient serverClient;
    private final List<String> statusIdList;
    private final List<T> result;

    public OperationFutureList(List<T> result, List<String> statusIdList, ServerClient serverClient) {
        this.serverClient = serverClient;
        this.statusIdList = statusIdList;
        this.result = result;
    }

    public OperationFutureList<T> waitUntilComplete() {
        statusIdList.forEach(this::waitUntilCompleteSingleJob);
        return this;
    }

    private void waitUntilCompleteSingleJob(String statusId) {
        if (statusId == null) {
            return;
        }

        for (;;) {
            String status = serverClient
                    .getJobStatus(statusId)
                    .getStatus();

            switch (status) {
                case "succeeded":
                    return;

                case "failed":
                case "unknown":
                    throw new ClcException("Operation Execution Failed");

                default:
                    try {
                        Thread.sleep(STATUS_POLLING_DELAY);
                    } catch (InterruptedException ex) {
                        throw Throwables.propagate(ex);
                    }
            }
        }
    }

    public List<T> getResult() {
        return result;
    }
}
