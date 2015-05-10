package com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future;

import com.centurylink.cloud.sdk.common.management.client.QueueClient;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.JobInfo;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.exceptions.JobFailedException;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.waiting.SingleWaitingLoop;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.waiting.WaitingLoop;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Ilya Drabenia
 */
public class SingleJobFuture extends AbstractSingleJobFuture {
    private final QueueClient queueClient;
    private final JobInfo jobInfo;

    public SingleJobFuture(JobInfo jobInfo, QueueClient queueClient) {
        this.jobInfo = jobInfo;
        this.queueClient = queueClient;
    }

    public SingleJobFuture(String statusId, QueueClient queueClient) {
        this.queueClient = checkNotNull(queueClient, "Queue client must be not a null");
        this.jobInfo = new JobInfo(statusId);
    }

    @Override
    public WaitingLoop waitingLoop() {
        return
            new SingleWaitingLoop(() -> {
                if (jobInfo.getStatusId() == null) {
                    throw new JobFailedException("Job %s doesn't contain Status ID", jobInfo);
                }

                String status = queueClient
                    .getJobStatus(jobInfo.getStatusId())
                    .getStatus();

                switch (status) {
                    case "succeeded":
                        return true;

                    case "failed":
                    case "unknown": {
                        throw new JobFailedException("The job %s is failed", jobInfo);
                    }

                    default:
                        return false;
                }
            });
    }

    @Override
    protected String operationInfo() {
        return jobInfo.toString();
    }
}
