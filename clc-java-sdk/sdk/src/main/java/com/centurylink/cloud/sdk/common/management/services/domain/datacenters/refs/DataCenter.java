package com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.core.services.refs.Reference;

/**
 * {@inheritDoc}
 */
public abstract class DataCenter implements Reference<DataCenterFilter> {
    public static final DataCenterByIdRef CA_VANCOUVER = refById("ca1");
    public static final DataCenterByIdRef CA_TORONTO_1 = refById("ca2");
    public static final DataCenterByIdRef CA_TORONTO_2 = refById("ca3");
    public static final DataCenterByIdRef DE_FRANKFURT = refById("de1");
    public static final DataCenterByIdRef GB_PORTSMOUTH = refById("gb1");
    public static final DataCenterByIdRef GB_SLOUGH = refById("gb3");
    public static final DataCenterByIdRef US_CENTRAL_CHICAGO = refById("il1");
    public static final DataCenterByIdRef US_CENTRAL_SALT_LAKE_CITY = refById("ut1");
    public static final DataCenterByIdRef US_EAST_NEW_YORK = refById("ny1");
    public static final DataCenterByIdRef US_EAST_STERLING = refById("va1");
    public static final DataCenterByIdRef US_WEST_SANTA_CLARA = refById("uc1");
    public static final DataCenterByIdRef US_WEST_SEATTLE = refById("wa1");

    /**
     * Method allow to refer datacenter by it's ID. Filtering is strong case sensitive.
     *
     * @param id is not null reference to datacenter
     * @return {@link DataCenterByIdRef}
     */
    public static DataCenterByIdRef refById(String id) {
        return new DataCenterByIdRef(id);
    }

    /**
     * Method allow to refer datacenter by keyword in it's name.
     * Matching use substring search. Comparison is case insensitive.
     *
     * @param name is not null keyword contains in target datacenter name
     * @return {@link DataCenterByNameRef}
     */
    public static DataCenterByNameRef refByName(String name) {
        return new DataCenterByNameRef(name);
    }

    @Override
    public String toString() {
        return this.toReadableString();
    }
}
