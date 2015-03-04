package com.centurylinkcloud.servers.domain.datacenter;

/**
 * @author ilya.drabenia
 */
public enum DataCenters {
    CA_VANCOUVER("CA1"),
    CA_TORONTO_1("CA2"),
    CA_TORONTO_2("CA3"),
    DE_FRANKFURT("DE1"),
    GB_PORTSMOUTH("GB1"),
    GB_SLOUGH("GB3"),
    US_CENTRAL_CHICAGO("IL1"),
    US_CENTRAL_SALT_LAKE_CITY("UT1"),
    US_EAST_NEW_YORK("NY1"),
    US_EAST_STERLING("VA1"),
    US_WEST_SANTA_CLARA("UC1"),
    US_WEST_SEATTLE("WA1");

    private final String id;

    private DataCenters(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
