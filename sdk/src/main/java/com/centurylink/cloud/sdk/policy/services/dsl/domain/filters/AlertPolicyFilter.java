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

package com.centurylink.cloud.sdk.policy.services.dsl.domain.filters;

import com.centurylink.cloud.sdk.core.function.Predicates;
import com.centurylink.cloud.sdk.core.function.Streams;
import com.centurylink.cloud.sdk.core.services.filter.AbstractResourceFilter;
import com.centurylink.cloud.sdk.core.services.filter.Filter;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.AndEvaluation;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.OrEvaluation;
import com.centurylink.cloud.sdk.policy.services.client.domain.AlertPolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.client.domain.AntiAffinityPolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.refs.AlertPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.centurylink.cloud.sdk.core.function.Predicates.combine;
import static com.centurylink.cloud.sdk.core.function.Predicates.in;
import static com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions.allItemsNotNull;
import static com.centurylink.cloud.sdk.core.preconditions.Preconditions.checkNotNull;
import static java.util.Arrays.asList;


/**
 * Class that used to filter policies
 *
 * @author Aliaksandr Krasitski
 */
public class AlertPolicyFilter extends AbstractResourceFilter<AlertPolicyFilter> {
    private List<String> ids = new ArrayList<>();
    private Predicate<AlertPolicyMetadata> predicate = Predicates.alwaysTrue();

    public AlertPolicyFilter() {

    }

    public AlertPolicyFilter(Predicate<AlertPolicyMetadata> policyFilter) {
        this.predicate = policyFilter;
    }

    /**
     * Method allow to filter policies by its IDs. Matching will be strong and case sensitive.
     *
     * @param ids is not null list of group IDs
     * @return {@link AlertPolicyFilter}
     */
    public AlertPolicyFilter id(String... ids) {
        return id(asList(ids));
    }

    /**
     * Method allow to filter policies by its IDs. Matching will be strong and case sensitive.
     *
     * @param ids is not null list of group IDs
     * @return {@link AlertPolicyFilter}
     */
    public AlertPolicyFilter id(List<String> ids) {
        checkNotNull(ids, "List of ids must be not a null");

        this.ids.addAll(ids);

        return this;
    }

    /**
     * Method allow to filter policies by its references.
     * @param policies is not null list of policy references
     * @return {@link AlertPolicyFilter}
     */
    public AlertPolicyFilter policies(AlertPolicy... policies) {
        allItemsNotNull(policies, "Anti-affinity Policies");

        evaluation = new AndEvaluation<>(evaluation, Filter.or(
            Streams.map(policies, AlertPolicy::asFilter)
        ), AlertPolicyMetadata::getId);

        return this;
    }

    /**
     * Method allow to filter policies by key phrase that contains in its name.
     * Filtering will be case insensitive and will use substring matching.
     *
     * @param subStrings is not null list of target policies names
     * @return {@link AlertPolicyFilter}
     */
    public AlertPolicyFilter nameContains(String... subStrings) {
        checkNotNull(subStrings, "Name match criteria must be not a null");

        predicate = predicate.and(combine(
            AlertPolicyMetadata::getName, in(asList(subStrings), Predicates::containsIgnoreCase)
        ));

        return this;
    }

    /**
     * Method allow to filter policies by names.
     * Filtering will be case insensitive and will use string equality comparison.
     *
     * @param names is not null list of policy names
     * @return {@link AlertPolicyFilter}
     */
    public AlertPolicyFilter names(String... names) {
        checkNotNull(names, "Name match criteria must be not a null");

        predicate = predicate.and(combine(
            AlertPolicyMetadata::getName, in(asList(names), Predicates::containsIgnoreCase)
        ));

        return this;
    }

    /**
     * Method allow to filter policies using predicate.
     *
     * @param filter is not null policy filtering predicate
     * @return {@link AlertPolicyFilter}
     */
    public AlertPolicyFilter where(Predicate<AlertPolicyMetadata> filter) {
        checkNotNull(filter, "Filter predicate must be not a null");

        this.predicate = this.predicate.and(filter);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AlertPolicyFilter and(AlertPolicyFilter otherFilter) {
        checkNotNull(otherFilter, "Other filter must be not a null");

        evaluation = new AndEvaluation<>(evaluation, otherFilter, AntiAffinityPolicyMetadata::getId);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AlertPolicyFilter or(AlertPolicyFilter otherFilter) {
        checkNotNull(otherFilter, "Other filter must be not a null");

        evaluation = new OrEvaluation<>(evaluation, otherFilter, AntiAffinityPolicyMetadata::getId);

        return this;
    }

    public Predicate<AlertPolicyMetadata> getPredicate() {
        return predicate;
    }

    public List<String> getIds() {
        return ids;
    }
}
