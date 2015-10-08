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

package com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.filter;

import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.function.Predicates;
import com.centurylink.cloud.sdk.core.function.Streams;
import com.centurylink.cloud.sdk.core.services.filter.AbstractResourceFilter;
import com.centurylink.cloud.sdk.core.services.filter.Filter;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.AndEvaluation;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.OrEvaluation;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.SingleFilterEvaluation;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.group.LoadBalancer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.centurylink.cloud.sdk.core.function.Predicates.*;
import static com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions.allItemsNotNull;
import static com.centurylink.cloud.sdk.core.preconditions.Preconditions.checkNotNull;
import static java.util.Arrays.asList;

public class LoadBalancerFilter extends AbstractResourceFilter<LoadBalancerFilter> {

    private List<String> ids = new ArrayList<>();
    private DataCenterFilter dataCenterFilter = new DataCenterFilter(alwaysTrue());
    private Predicate<LoadBalancerMetadata> predicate = alwaysTrue();

    public LoadBalancerFilter() {
    }

    public LoadBalancerFilter(DataCenterFilter dataCenterFilter, Predicate<LoadBalancerMetadata> predicate) {
        this.dataCenterFilter = dataCenterFilter;
        this.predicate = predicate;
    }

    /**
     * Method allow to provide custom filter predicate
     *
     * @param predicate is not null filtering expression
     * @return {@link LoadBalancerFilter}
     * @throws NullPointerException if {@code predicate} is null
     */
    public LoadBalancerFilter dataCentersWhere(Predicate<DataCenterMetadata> predicate) {
        checkNotNull(predicate, "Predicate must be not a null");

        dataCenterFilter.where(predicate);
        return this;
    }

    /**
     * Method allow to filter data centers by IDs. Filtering is strong case sensitive.
     *
     * @param ids the array of data center id
     * @return {@link LoadBalancerFilter}
     */
    public LoadBalancerFilter dataCenters(String... ids) {
        allItemsNotNull(ids, "Datacenter ID list");

        dataCenterFilter.id(ids);

        return this;
    }

    /**
     * Method allow to filter data centers by references.
     *
     * @param dataCenters is list of references to target dataCenter
     * @return {@link LoadBalancerFilter}
     * @throws NullPointerException if {@code dataCenters} is null
     */
    public LoadBalancerFilter dataCenters(DataCenter... dataCenters) {
        allItemsNotNull(dataCenters, "Datacenter references");

        dataCenterFilter.dataCenters(dataCenters);

        return this;
    }

    /**
     * Method allow to filter data centers by name.
     * Filtering is case insensitive and occurs using substring search.
     *
     * @param names is a not null list of name substrings
     * @return {@link LoadBalancerFilter}
     * @throws NullPointerException if {@code names} is null
     */
    public LoadBalancerFilter dataCenterNameContains(String... names) {
        allItemsNotNull(names, "Name subStrings");

        dataCenterFilter.nameContains(names);

        return this;
    }

    /**
     * Method allow to find load balancers that contains some substring in name.
     * Filtering is case insensitive.
     *
     * @param names is not null list of name substrings
     * @return {@link LoadBalancerFilter}
     * @throws NullPointerException if {@code names} is null
     */
    public LoadBalancerFilter nameContains(String... names) {
        allItemsNotNull(names, "Load balancer");

        predicate = predicate.and(combine(
            LoadBalancerMetadata::getName, in(asList(names), Predicates::containsIgnoreCase)
            ));

        return this;
    }

    /**
     * Method allow to find load balancers by its names
     * Filtering is case sensitive.
     *
     * @param names is a set of names
     * @return {@link LoadBalancerFilter}
     */
    public LoadBalancerFilter names(String... names) {
        allItemsNotNull(names, "Load balancer names");

        predicate = predicate.and(combine(
            LoadBalancerMetadata::getName, in(names)
        ));

        return this;
    }

    /**
     * Method allow to find load balancers that contains {@code substring} in description
     * Filtering is case insensitive.
     *
     * @param subStrings is a set of descriptions
     * @return {@link LoadBalancerFilter}
     */
    public LoadBalancerFilter descriptionContains(String... subStrings) {
        allItemsNotNull(subStrings, "Load balancer description subStrings");

        predicate = predicate.and(combine(
                LoadBalancerMetadata::getDescription, in(asList(subStrings), Predicates::containsIgnoreCase)
        ));

        return this;
    }

    public LoadBalancerFilter loadBalancers(LoadBalancer... loadBalancers) {
        allItemsNotNull(loadBalancers, "LoadBalancer references");

        predicate = predicate.and(Filter.or(
                Streams.map(loadBalancers, LoadBalancer::asFilter)
        ).getPredicate());

        return this;
    }

    /**
     * Method allow to specify custom template filtering predicate
     *
     * @param predicate is not null filtering expression
     * @return {@link LoadBalancerFilter}
     */
    public LoadBalancerFilter where(Predicate<LoadBalancerMetadata> predicate) {
        checkNotNull(predicate, "Predicate must be not a null");

        this.predicate = this.predicate.and(predicate);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoadBalancerFilter and(LoadBalancerFilter otherFilter) {
        checkNotNull(otherFilter, "Other filter must be not a null");

        if (evaluation instanceof SingleFilterEvaluation &&
            otherFilter.evaluation instanceof SingleFilterEvaluation) {
            return
                new LoadBalancerFilter(
                    getDataCenterFilter().and(otherFilter.getDataCenterFilter()),
                    getPredicate().and(otherFilter.getPredicate())
                );
        } else {
            evaluation = new AndEvaluation<>(evaluation, otherFilter, LoadBalancerMetadata::getName);

            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoadBalancerFilter or(LoadBalancerFilter otherFilter) {
        checkNotNull(otherFilter, "Other filter must be not a null");

        evaluation = new OrEvaluation<>(evaluation, otherFilter, LoadBalancerMetadata::getName);

        return this;
    }

    public LoadBalancerFilter id(String... ids) {
        return id(asList(ids));
    }

    public LoadBalancerFilter id(List<String> ids) {
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

    public Predicate<LoadBalancerMetadata> getPredicate() {
        return predicate;
    }
}
