package com.centurylink.cloud.sdk.core.datacenters.services.domain.filters;

import com.centurylink.cloud.sdk.core.datacenters.client.domain.DataCenterMetadata;

import java.util.List;
import java.util.function.Predicate;

import static com.centurylink.cloud.sdk.core.services.filter.Filters.containsIgnoreCase;
import static com.centurylink.cloud.sdk.core.services.filter.Filters.equalsIgnoreCase;

/**
 * @author Ilya Drabenia
 */
public class DataCentersFilter {
    private List<String> ids;
    private Predicate<DataCenterMetadata> predicate;

    public DataCentersFilter() {
    }

    public DataCentersFilter(Predicate<DataCenterMetadata> predicate) {
        this.predicate = predicate;
    }

    public List<String> getIds() {
        return ids;
    }

    public Predicate<DataCenterMetadata> getPredicate() {
        return predicate;
    }

    public DataCentersFilter filter(Predicate<DataCenterMetadata> predicate) {
        this.predicate = (this.predicate == null) ? predicate : this.predicate.and(predicate);
        return this;
    }

    public static DataCentersFilter where(Predicate<DataCenterMetadata> predicate) {
        return (DataCentersFilter) predicate;
    }

    public static DataCentersFilter whereNameContains(String name) {
        return new DataCentersFilter(d -> containsIgnoreCase(d.getName(), name));
    }

    public static DataCentersFilter whereIdIs(String id) {
        return new DataCentersFilter(d -> equalsIgnoreCase(d.getId(), id));
    }

}
