package com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.filters;

import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.services.filter.Filter;
import com.centurylink.cloud.sdk.core.services.function.Predicates;
import com.centurylink.cloud.sdk.core.services.filter.Filters;

import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.services.filter.Filters.containsIgnoreCase;
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
    public DataCenterFilter filter(Predicate<DataCenterMetadata> predicate) {
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
    public DataCenterFilter idIn(String... ids) {
        this.predicate =
            Stream
                .of(ids)
                .filter(id -> id != null)
                .map(id ->
                    (Predicate<DataCenterMetadata>) m -> Filters.equals(m.getId(), id)
                )
                .reduce(Predicates.alwaysFalse(),
                    (previousPredicate, item) -> previousPredicate.or(item)
                );

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
        return new DataCenterFilter(d -> containsIgnoreCase(d.getName(), name));
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
