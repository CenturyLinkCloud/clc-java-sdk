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

package com.centurylink.cloud.sdk.core.services.filter;

import com.centurylink.cloud.sdk.core.pool.ClcThreadPool;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.preconditions.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * @author Ilya Drabenia
 */
public interface FilterService<F extends Filter<F>, M> {

    static final ForkJoinPool POOL = ClcThreadPool.getPool();

    /**
     * Method find all resources satisfied by specified filter
     *
     * @param filter is not null search filter
     * @return list of found resource
     */
    default List<M> find(F filter) {
        checkNotNull(filter, "Filter must be not a null");

        try {
            return POOL.submit(() ->
                findLazy(filter).parallel().collect(toList())
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            return findLazy(filter).collect(toList());
        }
    }

    /**
     * Method find all resources satisfied by specified filter
     *
     * @param filter is not null search filter
     * @return stream of found resource
     */
    Stream<M> findLazy(F filter);

}
