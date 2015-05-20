/*
 * (c) 2015 CenturyLink Cloud. All Rights Reserved.
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

package com.centurylink.cloud.sdk.common.management.services.domain.queue;

import com.centurylink.cloud.sdk.common.management.client.QueueClient;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.JobFuture;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.NoWaitingJobFuture;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.ParallelJobsFuture;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.SingleJobFuture;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.centurylink.cloud.sdk.core.function.Streams.map;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * @author ilya.drabenia
 */
public class OperationFuture<T> {
    private final JobFuture waiting;
    private final T result;

    public OperationFuture(T result, String statusId, QueueClient queueClient) {
        this(result, new SingleJobFuture(statusId, queueClient));
    }

    public OperationFuture(T result, List<String> statusIds, QueueClient queueClient) {
        this(
            result,
            new ParallelJobsFuture(
                checkNotNull(statusIds)
                    .stream()
                    .map(status -> new SingleJobFuture(status, queueClient))
                    .collect(toList())
            )
        );
    }

    public OperationFuture(T result, JobFuture waiting) {
        this.waiting = waiting;
        this.result = result;
    }

    public OperationFuture<T> waitUntilComplete() {
        waiting.waitUntilComplete();
        return this;
    }

    public OperationFuture<T> waitUntilComplete(Duration timeout) {
        waiting.waitUntilComplete(timeout);
        return this;
    }

    public T getResult() {
        return result;
    }

    public CompletableFuture<T> waitAsync() {
        return waiting.waitAsync().thenApply(i -> getResult());
    }

    private CompletableFuture<T> future(Consumer<BiConsumer<Void, ? extends Throwable>> listener) {
        CompletableFuture<T> future = new CompletableFuture<>();

        listener.accept((result, error) -> {
            if (error == null) {
                future.complete(getResult());
            } else {
                future.completeExceptionally(error);
            }
        });

        return future;
    }

    @SafeVarargs
    public static <T> OperationFuture<List<T>> waitUntilComplete(OperationFuture<T>... operations) {
        checkArgument(operations.length >= 1);

        return
            new OperationFuture<>(
                map(operations, op -> op.waitUntilComplete().getResult()),
                new NoWaitingJobFuture()
            );
    }

    public JobFuture jobFuture() {
        return waiting;
    }

}
