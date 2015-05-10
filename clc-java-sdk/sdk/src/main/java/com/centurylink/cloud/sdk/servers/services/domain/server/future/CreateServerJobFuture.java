package com.centurylink.cloud.sdk.servers.services.domain.server.future;

import com.centurylink.cloud.sdk.common.management.client.QueueClient;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.SingleJobFuture;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.waiting.SingleWaitingLoop;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.waiting.WaitingLoop;
import com.centurylink.cloud.sdk.servers.client.ServerClient;

/**
 * @author Ilya Drabenia
 */
public class CreateServerJobFuture extends SingleJobFuture {
    private final String serverId;
    private final ServerClient serverClient;

    public CreateServerJobFuture(String statusId, String serverId, QueueClient queueClient, ServerClient serverClient) {
        super(statusId, queueClient);
        this.serverId = serverId;
        this.serverClient = serverClient;
    }

    @Override
    public WaitingLoop waitingLoop() {
        return
            super
                .waitingLoop()
                .andThen(new SingleWaitingLoop(() -> {
                    String status = serverClient
                        .findServerById(serverId)
                        .getStatus();

                    return "active".equals(status);
                }));
    }
}
