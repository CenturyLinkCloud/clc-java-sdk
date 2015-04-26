package com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.filters;

import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenterRef;
import com.centurylink.cloud.sdk.core.services.filter.Filter;
import com.centurylink.cloud.sdk.core.services.function.Predicates;

import java.util.function.Predicate;

import static com.centurylink.cloud.sdk.core.services.function.Predicates.combine;
import static com.centurylink.cloud.sdk.core.services.function.Predicates.containsIgnoreCase;
import static com.centurylink.cloud.sdk.core.services.function.Predicates.in;
import static com.centurylink.cloud.sdk.core.services.function.Streams.map;
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

    public DataCenterFilter in(DataCenterRef... dataCenterRefs) {
        predicate = predicate.and(Filter.or(
            map(dataCenterRefs, DataCenterRef::asFilter)
        ).getPredicate());

        return this;
    }

    /**
     * Method allow to filter data centers by name.
     * Filtering is case insensitive and occurs using substring search.
     *
     * @param name
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
