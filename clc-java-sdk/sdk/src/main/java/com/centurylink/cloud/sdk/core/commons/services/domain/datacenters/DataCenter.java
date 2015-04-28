package com.centurylink.cloud.sdk.core.commons.services.domain.datacenters;

import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenterByIdRef;

import static com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter.refById;

/**
 * Class represents CenturyLink data center in specified location
 *
 * @author ilya.drabenia
 */
public class DataCenter {
    public static DataCenterByIdRef CA_VANCOUVER = refById("ca1");
    public static DataCenterByIdRef CA_TORONTO_1 = refById("ca2");
    public static DataCenterByIdRef CA_TORONTO_2 = refById("ca3");
    public static DataCenterByIdRef DE_FRANKFURT = refById("de1");
    public static DataCenterByIdRef GB_PORTSMOUTH = refById("gb1");
    public static DataCenterByIdRef GB_SLOUGH = refById("gb3");
    public static DataCenterByIdRef US_CENTRAL_CHICAGO = refById("il1");
    public static DataCenterByIdRef US_CENTRAL_SALT_LAKE_CITY = refById("ut1");
    public static DataCenterByIdRef US_EAST_NEW_YORK = refById("ny1");
    public static DataCenterByIdRef US_EAST_STERLING = refById("va1");
    public static DataCenterByIdRef US_WEST_SANTA_CLARA = refById("uc1");
    public static DataCenterByIdRef US_WEST_SEATTLE = refById("wa1");
}
