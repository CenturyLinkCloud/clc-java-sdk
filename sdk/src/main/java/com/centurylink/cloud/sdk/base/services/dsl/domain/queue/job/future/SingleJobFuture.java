/*
 * (c) 2015 CenturyLink. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future;

import com.centurylink.cloud.sdk.base.services.client.QueueClient;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.JobInfo;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.exceptions.JobFailedException;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.waiting.SingleWaitingLoop;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.waiting.WaitingLoop;

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
