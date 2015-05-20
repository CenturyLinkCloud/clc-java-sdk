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

package com.centurylink.cloud.sdk.core.services.filter.evaluation;

import com.centurylink.cloud.sdk.core.services.filter.Filter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Ilya Drabenia
 */
@SuppressWarnings("unchecked")
public class AndEvaluation<T extends Filter<T>> extends LogicalOperatorEvaluation<T> {

    public <K, V> AndEvaluation(FilterEvaluation<T> evaluation, T filter,
                                Function<K, V> identityKeyExtractor) {
        super(evaluation, filter, identityKeyExtractor);
    }

    @SuppressWarnings("unchecked")
    private <K> Predicate<K> intersectionByKey() {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();

        return t -> seen.putIfAbsent(identityKeyExtractor.apply(t), Boolean.TRUE) != null;
    }

    @Override
    public <K> Stream<K> applyFindLazy(Function<T, Stream<K>> func) {
        return
            Stream
                .concat(
                    evaluation.applyFindLazy(func),
                    filter.applyFindLazy(func)
                )
                .filter(intersectionByKey());
    }
}
