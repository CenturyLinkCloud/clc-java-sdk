package com.centurylink.cloud.sdk.core.datacenters.services.domain.filters;

import com.centurylink.cloud.sdk.core.datacenters.client.domain.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.services.filter.EmptyPredicate;
import com.centurylink.cloud.sdk.core.services.filter.Filters;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.services.filter.Filters.containsIgnoreCase;
import static com.centurylink.cloud.sdk.core.services.filter.Filters.equalsIgnoreCase;

/**
 * @author Ilya Drabenia
 */
public class DataCenterFilter {
    private Predicate<DataCenterMetadata> predicate = new EmptyPredicate<>();

    public DataCenterFilter() {
    }

    public DataCenterFilter(Predicate<DataCenterMetadata> predicate) {
        this.predicate = predicate;
    }

    public Predicate<DataCenterMetadata> getPredicate() {
        return predicate;
    }

    public DataCenterFilter filter(Predicate<DataCenterMetadata> predicate) {
        this.predicate = (this.predicate == null) ? predicate : this.predicate.and(predicate);
        return this;
    }

    public DataCenterFilter idIn(String... ids) {
        this.predicate =
            Stream
                .of(ids)
                .filter(id -> id != null)
                .map(id ->
                    (Predicate<DataCenterMetadata>) m -> Filters.equals(m.getId(), id)
                )
                .reduce(new EmptyPredicate<>(),
                    (previousPredicate, item) -> previousPredicate.and(item)
                );

        return this;
    }

    public DataCenterFilter nameContains(String name) {
        return new DataCenterFilter(d -> containsIgnoreCase(d.getName(), name));
    }

}
