package com.centurylink.cloud.sdk.servers.services.domain;

import com.centurylink.cloud.sdk.core.exceptions.ClcException;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.google.common.base.Throwables;

/**
 * @author ilya.drabenia
 */
public class Response<T> {
    public static final long STATUS_POLLING_DELAY = 400L;

    private final ServerClient serverClient;
    private final String statusId;

    private final T result;

    public Response(T result, String statusId, ServerClient serverClient) {
        this.serverClient = serverClient;
        this.statusId = statusId;
        this.result = result;
    }

    public Response<T> waitUntilComplete() {
        for (;;) {
            String status = serverClient
                    .getJobStatus(statusId)
                    .getStatus();

            switch (status) {
                case "succeeded":
                    return this;

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

    public T getResult() {
        return result;
    }
}
