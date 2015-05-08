package com.centurylink.cloud.sdk.common.management.services.domain.queue.future.job;

import com.centurylink.cloud.sdk.common.management.client.QueueClient;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.future.OperationFailedException;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.future.job.waiting.SingleWaitingLoop;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.future.job.waiting.WaitingLoop;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Ilya Drabenia
 */
public class SingleJobFuture<T> extends AbstractSingleJobFuture {
    private final QueueClient queueClient;
    private final JobInfo<T> jobInfo;

    public SingleJobFuture(JobInfo<T> jobInfo, QueueClient queueClient) {
        this.jobInfo = jobInfo;
        this.queueClient = queueClient;
    }

    public SingleJobFuture(String statusId, QueueClient queueClient) {
        this.queueClient = checkNotNull(queueClient, "Queue client must be not a null");
        this.jobInfo = new JobInfo<>(null, checkNotNull(statusId, "Status ID must be not a null"));
    }

    @Override
    public WaitingLoop waitingLoop() {
        return
            new SingleWaitingLoop(() -> {
                if (jobInfo.getStatusId() == null) {
                    throw new OperationFailedException(
                        "Job for resource %s failed because statusId is null",
                        jobInfo.getResource()
                    );
                }

                String status = queueClient
                    .getJobStatus(jobInfo.getStatusId())
                    .getStatus();

                switch (status) {
                    case "succeeded":
                        if (jobInfo.getResource() != null)
                            jobInfo.getResource().future()
                                .complete(jobInfo.getResource().getArgument());
                        return true;

                    case "failed":
                    case "unknown":
                        throw new OperationFailedException(
                            "The job %s for resource %s is failed",
                            jobInfo.getStatusId(), jobInfo.getResource()
                        );

                    default:
                        return false;
                }
            });
    }

    @Override
    protected String operationInfo() {
        return jobInfo.getResource().toString();
    }
}
