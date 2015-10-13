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

package com.centurylink.cloud.sdk.core.services;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author Ilya Drabenia
 */
public abstract class SdkThreadPool {

    private static final Executor threadPool = Executors.newCachedThreadPool();
    private static final int PARALLELISM_LEVEL = 32;
    private static ForkJoinPool pool = new ForkJoinPool(PARALLELISM_LEVEL);

    public static Executor get() {
        return threadPool;
    }

    public static <M> List<M> executeParallel(Stream<M> stream) {
        try {
            return pool.submit(() ->
                    stream.parallel().collect(toList())
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            return stream.collect(toList());
        }
    }

    public static <M> void executeParallel(Stream<M> stream, Consumer<M> consumer) {
        try {
            pool.submit(() ->
                    stream.parallel().forEach(consumer)
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            stream.forEach(consumer);
        }
    }
}
