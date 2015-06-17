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

package com.centurylink.cloud.sdk.server.services.dsl.domain.server.future;

import com.centurylink.cloud.sdk.base.services.client.QueueClient;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.SingleJobFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.waiting.SingleWaitingLoop;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.waiting.WaitingLoop;
import com.centurylink.cloud.sdk.server.services.client.ServerClient;

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
