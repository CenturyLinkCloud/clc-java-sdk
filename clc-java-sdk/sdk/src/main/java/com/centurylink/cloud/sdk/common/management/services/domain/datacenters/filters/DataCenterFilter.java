package com.centurylink.cloud.sdk.common.management.services.domain.datacenters.filters;

import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.services.filter.Filter;
import com.centurylink.cloud.sdk.core.function.Predicates;

import java.util.function.Predicate;

import static com.centurylink.cloud.sdk.core.function.Predicates.combine;
import static com.centurylink.cloud.sdk.core.function.Predicates.containsIgnoreCase;
import static com.centurylink.cloud.sdk.core.function.Predicates.in;
import static com.centurylink.cloud.sdk.core.function.Streams.map;
import static com.google.common.base.Preconditions.checkNotNull;

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
        checkNotNull(predicate, "Predicate must be not a null");

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
        predicate = predicate.and(Filter.or(
            map(dataCenterRefs, DataCenter::asFilter)
        ).getPredicate());

        return this;
    }

    /**
     * Method allow to filter data centers by name.
     * Filtering is case insensitive and occurs using substring search.
     *
     * @param name is a not null name substring
     * @return {@link DataCenterFilter}
     */
    public DataCenterFilter nameContains(String name) {
        checkNotNull(name, "Name must be not a null");

        predicate = predicate.and(combine(
            DataCenterMetadata::getName, containsIgnoreCase(name)
        ));

        return this;
    }

    @Override
    public DataCenterFilter and(DataCenterFilter otherFilter) {
        checkNotNull(otherFilter, "Other filter must be not a null");

        return new DataCenterFilter(
            getPredicate().and(otherFilter.getPredicate())
        );
    }

    @Override
    public DataCenterFilter or(DataCenterFilter otherFilter) {
        checkNotNull(otherFilter, "Other filter must be not a null");

        return new DataCenterFilter(
            getPredicate().or(otherFilter.getPredicate())
        );
    }
}
