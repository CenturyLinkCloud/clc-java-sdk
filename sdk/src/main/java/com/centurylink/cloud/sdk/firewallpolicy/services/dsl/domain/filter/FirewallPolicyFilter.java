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

package com.centurylink.cloud.sdk.firewallpolicy.services.dsl.domain.filter;

import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.function.Streams;
import com.centurylink.cloud.sdk.core.services.filter.AbstractResourceFilter;
import com.centurylink.cloud.sdk.core.services.filter.Filter;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.AndEvaluation;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.OrEvaluation;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.SingleFilterEvaluation;
import com.centurylink.cloud.sdk.firewallpolicy.services.dsl.domain.FirewallPolicyMetadata;
import com.centurylink.cloud.sdk.firewallpolicy.services.dsl.domain.refs.FirewallPolicy;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.centurylink.cloud.sdk.core.function.Predicates.alwaysTrue;
import static com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions.allItemsNotNull;
import static com.centurylink.cloud.sdk.core.preconditions.Preconditions.checkNotNull;
import static java.util.Arrays.asList;

public class FirewallPolicyFilter extends AbstractResourceFilter<FirewallPolicyFilter> {

    private List<String> ids = new ArrayList<>();
    private DataCenterFilter dataCenterFilter = new DataCenterFilter(alwaysTrue());
    private Predicate<FirewallPolicyMetadata> predicate = alwaysTrue();
    private String destinationAccountAlias;

    public FirewallPolicyFilter() {}

    public FirewallPolicyFilter(DataCenterFilter dataCenterFilter, Predicate<FirewallPolicyMetadata> predicate) {
        this.dataCenterFilter = dataCenterFilter;
        this.predicate = predicate;
    }

    public FirewallPolicyFilter(
            DataCenterFilter dataCenterFilter,
            Predicate<FirewallPolicyMetadata> predicate,
            String destinationAccountAlias
    ) {
        this.dataCenterFilter = dataCenterFilter;
        this.predicate = predicate;
        this.destinationAccountAlias = destinationAccountAlias;
    }

    /**
     * Method allow to provide custom filter predicate
     *
     * @param predicate is not null filtering expression
     * @return {@link FirewallPolicyFilter}
     * @throws NullPointerException if {@code predicate} is null
     */
    public FirewallPolicyFilter dataCentersWhere(Predicate<DataCenterMetadata> predicate) {
        checkNotNull(predicate, "Predicate must be not a null");

        dataCenterFilter.where(predicate);
        return this;
    }

    /**
     * Method allow to filter data centers by IDs. Filtering is strong case sensitive.
     *
     * @param ids the array of data center id
     * @return {@link FirewallPolicyFilter}
     */
    public FirewallPolicyFilter dataCenters(String... ids) {
        allItemsNotNull(ids, "Datacenter ID list");

        dataCenterFilter.id(ids);

        return this;
    }

    /**
     * Method allow to filter data centers by references.
     *
     * @param dataCenters is list of references to target dataCenter
     * @return {@link FirewallPolicyFilter}
     * @throws NullPointerException if {@code dataCenters} is null
     */
    public FirewallPolicyFilter dataCenters(DataCenter... dataCenters) {
        allItemsNotNull(dataCenters, "FirewallPolicy references");

        dataCenterFilter.dataCenters(dataCenters);

        return this;
    }

    public FirewallPolicyFilter firewallPolicies(FirewallPolicy... firewallPolicies) {
        allItemsNotNull(firewallPolicies, "FirewallPolicy references");

        predicate = predicate.and(Filter.or(
                Streams.map(firewallPolicies, FirewallPolicy::asFilter)
        ).getPredicate());

        return this;
    }

    /**
     * Method allow to specify custom template filtering predicate
     *
     * @param predicate is not null filtering expression
     * @return {@link FirewallPolicyFilter}
     */
    public FirewallPolicyFilter where(Predicate<FirewallPolicyMetadata> predicate) {
        checkNotNull(predicate, "Predicate must be not a null");

        this.predicate = this.predicate.and(predicate);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FirewallPolicyFilter and(FirewallPolicyFilter otherFilter) {
        checkNotNull(otherFilter, "Other filter must be not a null");

        if (evaluation instanceof SingleFilterEvaluation &&
            otherFilter.evaluation instanceof SingleFilterEvaluation) {
            return
                new FirewallPolicyFilter(
                    getDataCenterFilter().and(otherFilter.getDataCenterFilter()),
                    getPredicate().and(otherFilter.getPredicate()),
                    destinationAccountAlias
                );
        }

        evaluation = new AndEvaluation<>(evaluation, otherFilter, LoadBalancerMetadata::getName);
        return this;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FirewallPolicyFilter or(FirewallPolicyFilter otherFilter) {
        checkNotNull(otherFilter, "Other filter must be not a null");

        evaluation = new OrEvaluation<>(evaluation, otherFilter, LoadBalancerMetadata::getName);

        return this;
    }

    public FirewallPolicyFilter id(String... ids) {
        return id(asList(ids));
    }

    public String getDestinationAccountAlias() {
        return destinationAccountAlias;
    }

    public FirewallPolicyFilter destinationAccountAlias(String destinationAccountAlias) {
        this.destinationAccountAlias = destinationAccountAlias;
        return this;
    }

    public FirewallPolicyFilter id(List<String> ids) {
        checkNotNull(ids, "List of ids must be not a null");
        this.ids.addAll(ids);

        return this;
    }

    public List<String> getIds() {
        return ids;
    }

    public DataCenterFilter getDataCenterFilter() {
        return dataCenterFilter;
    }

    public Predicate<FirewallPolicyMetadata> getPredicate() {
        return predicate;
    }
}
