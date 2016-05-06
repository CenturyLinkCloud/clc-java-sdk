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

import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.AbstractSingleJobFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.waiting.SingleWaitingLoop;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.waiting.WaitingLoop;
import com.centurylink.cloud.sdk.core.exceptions.ClcException;
import com.centurylink.cloud.sdk.server.services.client.ServerClient;

/**
 * @author Aliaksandr Krasitski
 */
public class CreateServerBlueprintJobFuture extends AbstractSingleJobFuture {
    private final String serverUuId;
    private final ServerClient serverClient;

    public CreateServerBlueprintJobFuture(String serverUuId, ServerClient serverClient) {
        this.serverUuId = serverUuId;
        this.serverClient = serverClient;
    }

    @Override
    protected String operationInfo() {
        return serverUuId;
    }

    @Override
    public WaitingLoop waitingLoop() {
        return
            new SingleWaitingLoop(() -> {
                try {
                    serverClient.findServerByUuid(serverUuId);
                    return true;
                } catch (ClcException e) {
                    return false;
                }
            });
    }
}
