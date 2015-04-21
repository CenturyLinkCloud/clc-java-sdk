package com.centurylink.cloud.sdk.servers.services.domain.ip;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Aliaksandr Krasitski
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SourceRestriction {
    private String cidr;

    public String getCidr() {
        return cidr;
    }

    public void setCidr(String cidr) {
        this.cidr = cidr;
    }

    public SourceRestriction() {}

    public SourceRestriction(String cidr) {
        this.cidr = cidr;
    }
}
