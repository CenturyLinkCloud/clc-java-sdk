package com.centurylink.cloud.sdk.servers.services.domain.ip;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by aliaksandr.krasitski on 4/16/2015.
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

    public SourceRestriction cidr(String cidr) {
        setCidr(cidr);
        return this;
    }
}
