package com.centurylink.cloud.sdk.commons.services.domain.queue.future.job;

import com.centurylink.cloud.sdk.commons.client.QueueClient;
import com.centurylink.cloud.sdk.commons.services.domain.queue.future.OperationFailedException;
import com.centurylink.cloud.sdk.commons.services.domain.queue.future.job.waiting.SingleWaitingLoop;
import com.centurylink.cloud.sdk.commons.services.domain.queue.future.job.waiting.WaitingLoop;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Ilya Drabenia
 */
public class SingleJobFuture extends AbstractSingleJobFuture {
    private final QueueClient queueClient;
    private final String statusId;

    public SingleJobFuture(String statusId, QueueClient queueClient) {
        this.queueClient = checkNotNull(queueClient, "Queue client must be not a null");
        this.statusId = checkNotNull(statusId, "Status ID must be not a null");
    }

    @Override
    public WaitingLoop waitingLoop() {
        return
            new SingleWaitingLoop(() -> {
                if (statusId == null) {
                    throw new OperationFailedException();
                }

                String status = queueClient
                    .getJobStatus(statusId)
                    .getStatus();

                switch (status) {
                    case "succeeded":
                        return true;

                    case "failed":
                    case "unknown":
                        throw new OperationFailedException("The job with status %s and id %s failed", status, statusId);

                    default:
                        return false;
                }
            });
    }

    @Override
    protected String operationInfo() {
        return statusId;
    }
}
