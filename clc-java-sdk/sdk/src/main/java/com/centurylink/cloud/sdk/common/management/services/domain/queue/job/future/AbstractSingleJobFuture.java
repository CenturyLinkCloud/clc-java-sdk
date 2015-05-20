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

package com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future;

import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.waiting.TimeoutInterceptor;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.waiting.WaitingLoop;
import com.centurylink.cloud.sdk.core.services.SdkThreadPool;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * @author Ilya Drabenia
 */
public abstract class AbstractSingleJobFuture implements JobFuture {

    @Override
    public void waitUntilComplete() {
        waitingLoop().get();
    }

    @Override
    public void waitUntilComplete(Duration timeout) {
        waitingLoop()
            .onIterationStarted(
                new TimeoutInterceptor(timeout, operationInfo())
            )
            .get();
    }

    @Override
    public CompletableFuture<Void> waitAsync() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        SdkThreadPool.get().execute(() -> {
            try {
                waitUntilComplete();
                future.complete(null);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    protected abstract String operationInfo();

    public abstract WaitingLoop waitingLoop();
}
