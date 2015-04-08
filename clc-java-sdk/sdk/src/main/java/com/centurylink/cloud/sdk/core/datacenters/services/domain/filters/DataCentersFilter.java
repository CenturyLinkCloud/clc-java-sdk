package com.centurylink.cloud.sdk.core.datacenters.services.domain.filters;

import com.centurylink.cloud.sdk.core.datacenters.client.domain.DataCenterMetadata;

import java.util.List;
import java.util.function.Predicate;

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

    public static boolean upperContains(String value, String substring) {
        return value.toUpperCase().contains(substring.toUpperCase());
    }

    public static DataCentersFilter whereNameContains(String name) {
        return new DataCentersFilter(d -> name != null && upperContains(d.getName(), name));
    }

    public static DataCentersFilter whereIdIs(String id) {
        return new DataCentersFilter(d -> id != null && d.getId().equalsIgnoreCase(id));
    }

}
