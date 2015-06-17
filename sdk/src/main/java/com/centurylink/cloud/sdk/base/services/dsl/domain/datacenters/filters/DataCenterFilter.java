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

package com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.filters;

import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.function.Predicates;
import com.centurylink.cloud.sdk.core.function.Streams;
import com.centurylink.cloud.sdk.core.services.filter.Filter;

import java.util.function.Predicate;

import static com.centurylink.cloud.sdk.core.function.Predicates.combine;
import static com.centurylink.cloud.sdk.core.function.Predicates.in;
import static com.centurylink.cloud.sdk.core.function.Streams.map;
import static com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions.allItemsNotNull;
import static com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions.notNull;
import static java.util.Arrays.asList;

/**
 * Criteria that used for specify needed data centers
 *
 * @author Ilya Drabenia
 */
public class DataCenterFilter implements Filter<DataCenterFilter> {
    private Predicate<DataCenterMetadata> predicate = Predicates.alwaysTrue();

    public DataCenterFilter() {
    }

    public DataCenterFilter(Predicate<DataCenterMetadata> predicate) {
        this.predicate = predicate;
    }

    public Predicate<DataCenterMetadata> getPredicate() {
        return predicate;
    }

    /**
     * Method allow to provide custom filter predicate
     *
     * @param predicate is not null filtering predicate
     * @return {@link DataCenterFilter}
     */
    public DataCenterFilter where(Predicate<DataCenterMetadata> predicate) {
        notNull(predicate, "Predicate must be not a null");

        this.predicate = this.predicate.and(predicate);

        return this;
    }

    /**
     * Method allow to filter data centers by IDs. Filtering is by full match.
     * Comparison is case insensitive.
     *
     * @param ids is not null target datacenter IDs
     * @return {@link DataCenterFilter}
     */
    public DataCenterFilter id(String... ids) {
        allItemsNotNull(ids, "Data center ID list");

        this.predicate = this.predicate.and(combine(
            DataCenterMetadata::getId, in(asList(ids), Predicates::equalsIgnoreCase)
        ));

        return this;
    }

    /**
     * Method allow to filter data centers by references.
     *
     * @param dataCenterRefs is list of references to target dataCenter
     * @return {@link DataCenterFilter}
     */
    public DataCenterFilter dataCenters(DataCenter... dataCenterRefs) {
        allItemsNotNull(dataCenterRefs, "Datacenter references");

        predicate = predicate.and(Filter.or(
            Streams.map(dataCenterRefs, DataCenter::asFilter)
        ).getPredicate());

        return this;
    }

    /**
     * Method allow to filter data centers by name.
     * Filtering is case insensitive and occurs using substring search.
     *
     * @param names is a list of not nul name substring
     * @return {@link DataCenterFilter}
     */
    public DataCenterFilter nameContains(String... names) {
        allItemsNotNull(names, "Name keywords");

        predicate = predicate.and(combine(
            DataCenterMetadata::getName, in(asList(names), Predicates::containsIgnoreCase)
        ));

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataCenterFilter and(DataCenterFilter otherFilter) {
        notNull(otherFilter, "Other filter must be not a null");

        return new DataCenterFilter(
            getPredicate().and(otherFilter.getPredicate())
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataCenterFilter or(DataCenterFilter otherFilter) {
        notNull(otherFilter, "Other filter must be not a null");

        return new DataCenterFilter(
            getPredicate().or(otherFilter.getPredicate())
        );
    }
}
