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

package com.centurylink.cloud.sdk.autoscalepolicy.services.dsl.domain.filter;

import com.centurylink.cloud.sdk.autoscalepolicy.services.dsl.domain.AutoscalePolicyMetadata;
import com.centurylink.cloud.sdk.autoscalepolicy.services.dsl.domain.refs.AutoscalePolicy;
import com.centurylink.cloud.sdk.core.function.Predicates;
import com.centurylink.cloud.sdk.core.function.Streams;
import com.centurylink.cloud.sdk.core.services.filter.AbstractResourceFilter;
import com.centurylink.cloud.sdk.core.services.filter.Filter;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.AndEvaluation;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.OrEvaluation;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.SingleFilterEvaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.centurylink.cloud.sdk.core.function.Predicates.alwaysTrue;
import static com.centurylink.cloud.sdk.core.function.Predicates.combine;
import static com.centurylink.cloud.sdk.core.function.Predicates.in;
import static com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions.allItemsNotNull;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;

public class AutoscalePolicyFilter extends AbstractResourceFilter<AutoscalePolicyFilter> {

    private List<String> ids = new ArrayList<>();
    private Predicate<AutoscalePolicyMetadata> predicate = alwaysTrue();

    public AutoscalePolicyFilter() {}

    public AutoscalePolicyFilter(Predicate<AutoscalePolicyMetadata> predicate) {
        this.predicate = predicate;
    }

    public AutoscalePolicyFilter autoscalePolicies(AutoscalePolicy... autoscalePolicies) {
        allItemsNotNull(autoscalePolicies, "Autoscale policy references");

        predicate = predicate.and(Filter.or(
                Streams.map(autoscalePolicies, AutoscalePolicy::asFilter)
        ).getPredicate());

        return this;
    }

    /**
     * Method allow to specify custom template filtering predicate
     *
     * @param predicate is not null filtering expression
     * @return {@link AutoscalePolicyFilter}
     */
    public AutoscalePolicyFilter where(Predicate<AutoscalePolicyMetadata> predicate) {
        checkNotNull(predicate, "Predicate must be not a null");

        this.predicate = this.predicate.and(predicate);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AutoscalePolicyFilter and(AutoscalePolicyFilter otherFilter) {
        checkNotNull(otherFilter, "Other filter must be not a null");

        if (evaluation instanceof SingleFilterEvaluation &&
            otherFilter.evaluation instanceof SingleFilterEvaluation) {
            return
                new AutoscalePolicyFilter(
                    getPredicate().and(otherFilter.getPredicate())
                );
        }

        evaluation = new AndEvaluation<>(evaluation, otherFilter, AutoscalePolicyMetadata::getName);
        return this;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AutoscalePolicyFilter or(AutoscalePolicyFilter otherFilter) {
        checkNotNull(otherFilter, "Other filter must be not a null");

        evaluation = new OrEvaluation<>(evaluation, otherFilter, AutoscalePolicyMetadata::getName);

        return this;
    }

    public AutoscalePolicyFilter id(String... ids) {
        return id(asList(ids));
    }

    public AutoscalePolicyFilter id(List<String> ids) {
        checkNotNull(ids, "List of ids must be not a null");
        this.ids.addAll(ids);

        return this;
    }

    public List<String> getIds() {
        return ids;
    }

    /**
     * Method allow to find autoscale policies that contains some substring in name.
     * Filtering is case insensitive.
     *
     * @param names is not null list of name substrings
     * @return {@link AutoscalePolicyFilter}
     * @throws NullPointerException if {@code names} is null
     */
    public AutoscalePolicyFilter nameContains(String... names) {
        allItemsNotNull(names, "Autoscale policy names");

        predicate = predicate.and(combine(
            AutoscalePolicyMetadata::getName, in(asList(names), Predicates::containsIgnoreCase)
        ));

        return this;
    }

    /**
     * Method allow to find autoscale policies by its names
     * Filtering is case sensitive.
     *
     * @param names is a set of names
     * @return {@link AutoscalePolicyFilter}
     */
    public AutoscalePolicyFilter names(String... names) {
        allItemsNotNull(names, "Autoscale policies names");

        predicate = predicate.and(combine(
            AutoscalePolicyMetadata::getName, in(names)
        ));

        return this;
    }

    public Predicate<AutoscalePolicyMetadata> getPredicate() {
        return predicate;
    }
}
