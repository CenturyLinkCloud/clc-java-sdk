package com.centurylink.cloud.sdk.servers.services.domain.server;

/**
 * @author ilya.drabenia
 */
public class Network {
    private String primaryDns;
    private String secondaryDns;

    public String getPrimaryDns() {
        return primaryDns;
    }

    public void setPrimaryDns(String primaryDns) {
        this.primaryDns = primaryDns;
    }

    public Network primaryDns(String primaryDns) {
        setPrimaryDns(primaryDns);
        return this;
    }

    public String getSecondaryDns() {
        return secondaryDns;
    }

    public void setSecondaryDns(String secondaryDns) {
        this.secondaryDns = secondaryDns;
    }

    public Network secondaryDns(String secondaryDns) {
        setSecondaryDns(secondaryDns);
        return this;
    }
}
