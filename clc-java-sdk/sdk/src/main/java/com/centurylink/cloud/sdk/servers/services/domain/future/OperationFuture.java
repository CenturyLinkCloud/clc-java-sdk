package com.centurylink.cloud.sdk.servers.services.domain.future;

import com.centurylink.cloud.sdk.core.exceptions.ClcException;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.google.common.base.Throwables;

/**
 * @author ilya.drabenia
 */
public class OperationFuture<T> {
    public static final long STATUS_POLLING_DELAY = 400L;

    private final ServerClient serverClient;
    private final String statusId;

    private final T result;

    public OperationFuture(T result, String statusId, ServerClient serverClient) {
        this.serverClient = serverClient;
        this.statusId = statusId;
        this.result = result;
    }

    public OperationFuture<T> waitUntilComplete() {
        if (statusId == null) {
            throw new OperationFailedException();
        }

        for (;;) {
            String status = serverClient
                .getJobStatus(statusId)
                .getStatus();

            switch (status) {
                case "succeeded":
                    return this;

                case "failed":
                case "unknown":
                    throw new OperationFailedException();

                default:
                    try {
                        Thread.sleep(STATUS_POLLING_DELAY);
                    } catch (InterruptedException ex) {
                        throw Throwables.propagate(ex);
                    }
            }
        }
    }

    public static void waitUntilCompleteMultipleJobs(OperationFuture... asyncResponses) {
        for (OperationFuture actionPromise : asyncResponses) {
            actionPromise.waitUntilComplete();
        }
    }

    public T getResult() {
        return result;
    }
}
