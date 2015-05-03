package com.centurylink.cloud.sdk.common.management.services.domain.datacenters.filters;

import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.function.Predicates;
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
     * @param predicate
     * @return {@link DataCenterFilter}
     */
    public DataCenterFilter where(Predicate<DataCenterMetadata> predicate) {
        notNull(predicate, "Predicate must be not a null");

        this.predicate = this.predicate.and(predicate);

        return this;
    }

    /**
     * Method allow to filter data centers by IDs. Filtering is strong case sensitive.
     *
     * @param ids
     * @return {@link DataCenterFilter}
     */
    public DataCenterFilter id(String... ids) {
        allItemsNotNull(ids, "Data center ID list");

        this.predicate = this.predicate.and(combine(
            DataCenterMetadata::getId, Predicates.in(ids)
        ));

        return this;
    }

    /**
     * Method allow to filter data centers by references.
     *
     * @param dataCenterRefs is list of references to target dataCenters
     * @return {@link DataCenterFilter}
     */
    public DataCenterFilter dataCenters(DataCenter... dataCenterRefs) {
        allItemsNotNull(dataCenterRefs, "Datacenter references");

        predicate = predicate.and(Filter.or(
            map(dataCenterRefs, DataCenter::asFilter)
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
