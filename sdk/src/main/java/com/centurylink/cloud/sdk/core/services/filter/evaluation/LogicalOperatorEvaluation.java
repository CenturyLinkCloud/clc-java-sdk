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

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Ilya Drabenia
 */
public class LogicalOperatorEvaluation<T extends Filter<T>> implements FilterEvaluation<T> {
    protected final FilterEvaluation<T> evaluation;
    protected final T filter;
    protected final Function identityKeyExtractor;

    public LogicalOperatorEvaluation(FilterEvaluation<T> evaluation, T filter,
                                     Function identityKeyExtractor) {
        this.evaluation = checkNotNull(evaluation);
        this.filter = checkNotNull(filter);
        this.identityKeyExtractor = checkNotNull(identityKeyExtractor);
    }
}
