package com.centurylinkcloud.servers.domain;

import com.centurylinkcloud.servers.client.ServerClient;
import com.google.common.base.Throwables;

/**
 * @author ilya.drabenia
 */
public class Response<T> {
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
                    throw new RuntimeException("Operation Execution Failed");

                default:
                    try {
                        Thread.sleep(400L);
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
