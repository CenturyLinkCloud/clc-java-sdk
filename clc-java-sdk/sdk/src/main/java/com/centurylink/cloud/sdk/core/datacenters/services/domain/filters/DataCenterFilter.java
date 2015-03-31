package com.centurylink.cloud.sdk.core.datacenters.services.domain.filters;

import com.centurylink.cloud.sdk.core.datacenters.client.domain.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenter;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.util.Objects;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Ilya Drabenia
 */
public interface DataCenterFilter extends Predicate<DataCenterMetadata> {

    static DataCenterFilter where(Predicate<DataCenterMetadata> predicate) {
        return (DataCenterFilter) predicate;
    }

    static boolean upperContains(String value, String substring) {
        return value.toUpperCase().contains(substring.toUpperCase());
    }

    static DataCenterFilter whereNameContains(String name) {
        return d -> name != null && upperContains(d.getName(), name);
    }

    static DataCenterFilter whereIdIs(String id) {
        return d -> id != null && d.getId().equalsIgnoreCase(id);
    }

}
