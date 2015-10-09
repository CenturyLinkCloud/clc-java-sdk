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

package com.centurylink.cloud.sdk.server.services.dsl.domain.network.filters;

import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.function.Predicates;
import com.centurylink.cloud.sdk.core.function.Streams;
import com.centurylink.cloud.sdk.core.services.filter.AbstractResourceFilter;
import com.centurylink.cloud.sdk.core.services.filter.Filter;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.AndEvaluation;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.OrEvaluation;
import com.centurylink.cloud.sdk.server.services.client.domain.network.NetworkMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.domain.network.refs.Network;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.centurylink.cloud.sdk.core.function.Predicates.combine;
import static com.centurylink.cloud.sdk.core.function.Predicates.in;
import static com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions.allItemsNotNull;
import static com.centurylink.cloud.sdk.core.preconditions.Preconditions.checkNotNull;
import static java.util.Arrays.asList;


/**
 * Class that used to filter networks
 *
 * @author Aliaksandr Krasitski
 */
public class NetworkFilter extends AbstractResourceFilter<NetworkFilter> {
    private List<String> ids = new ArrayList<>();
    private DataCenterFilter dataCenterFilter = new DataCenterFilter(Predicates.alwaysTrue());
    private Predicate<NetworkMetadata> predicate = Predicates.alwaysTrue();

    public NetworkFilter() {

    }

    public NetworkFilter(Predicate<NetworkMetadata> networkFilter) {
        this.predicate = networkFilter;
    }

    /**
     * Method allow to restrict target networks using data centers in which this networks exists.
     *
     * @param dataCenters is not null list of data center references
     * @return {@link NetworkFilter}
     */
    public NetworkFilter dataCenters(DataCenter... dataCenters) {
        dataCenterFilter.dataCenters(dataCenters);

        return this;
    }

    /**
     * Method allow to provide filtering predicate that restrict network by data centers that contains its.
     *
     * @param predicate is not null filtering predicate
     * @return {@link NetworkFilter}
     */
    public NetworkFilter dataCentersWhere(Predicate<DataCenterMetadata> predicate) {
        dataCenterFilter.where(
            dataCenterFilter.getPredicate().or(predicate)
        );

        return this;
    }

    /**
     * Method allow to provide data center filter that allow to restrict networks by data centers that contains its
     *
     * @param filter is not null data center filter
     * @return {@link NetworkFilter}
     */
    public NetworkFilter dataCentersWhere(DataCenterFilter filter) {
        dataCenterFilter = dataCenterFilter.and(filter);
        return this;
    }

    /**
     * Method allow to filter networks by its IDs. Matching will be strong and case sensitive.
     *
     * @param ids is not null list of network IDs
     * @return {@link NetworkFilter}
     */
    public NetworkFilter id(String... ids) {
        return id(asList(ids));
    }

    public NetworkFilter id(List<String> ids) {
        checkNotNull(ids, "List of ids must be not a null");

        this.ids.addAll(ids);

        return this;
    }

    public NetworkFilter networks(Network... networks) {
        allItemsNotNull(networks, "Networks");

        evaluation = new AndEvaluation<>(evaluation, Filter.or(
            Streams.map(networks, Network::asFilter)
        ), NetworkMetadata::getId);

        return this;
    }

    /**
     * Method allow to filter networks by key phrase that contains in its name.
     * Filtering will be case insensitive and will use substring matching.
     *
     * @param subStrings is not null list of target network names
     * @return {@link NetworkFilter}
     */
    public NetworkFilter nameContains(String... subStrings) {
        checkNotNull(subStrings, "Name match criteria must be not a null");

        predicate = predicate.and(combine(
            NetworkMetadata::getName, in(asList(subStrings), Predicates::containsIgnoreCase)
        ));

        return this;
    }

    /**
     * Method allow to filter networks by names.
     * Filtering will be case insensitive and will use string equality comparison.
     *
     * @param names is not null list of network names
     * @return {@link NetworkFilter}
     */
    public NetworkFilter names(String... names) {
        checkNotNull(names, "Name match criteria must be not a null");

        predicate = predicate.and(combine(
            NetworkMetadata::getName, in(asList(names), Predicates::containsIgnoreCase)
        ));

        return this;
    }

    /**
     * Method allow to filter networks using predicate.
     *
     * @param filter is not null network filtering predicate
     * @return {@link NetworkFilter}
     */
    public NetworkFilter where(Predicate<NetworkMetadata> filter) {
        checkNotNull(filter, "Filter predicate must be not a null");

        this.predicate = this.predicate.and(filter);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkFilter and(NetworkFilter otherFilter) {
        checkNotNull(otherFilter, "Other filter must be not a null");

        evaluation = new AndEvaluation<>(evaluation, otherFilter, NetworkMetadata::getId);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkFilter or(NetworkFilter otherFilter) {
        checkNotNull(otherFilter, "Other filter must be not a null");

        evaluation = new OrEvaluation<>(evaluation, otherFilter, NetworkMetadata::getId);

        return this;
    }

    public DataCenterFilter getDataCenterFilter() {
        return dataCenterFilter;
    }

    public Predicate<NetworkMetadata> getPredicate() {
        return predicate;
    }

    public List<String> getIds() {
        return ids;
    }
}
