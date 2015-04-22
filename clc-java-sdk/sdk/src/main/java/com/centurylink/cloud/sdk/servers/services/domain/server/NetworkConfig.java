package com.centurylink.cloud.sdk.servers.services.domain.server;

import com.centurylink.cloud.sdk.networks.services.domain.refs.NetworkRef;
import com.centurylink.cloud.sdk.servers.client.domain.ip.PublicIpAddressRequest;

/**
 * @author ilya.drabenia
 */
public class NetworkConfig {
    private NetworkRef network;
    private String primaryDns;
    private String secondaryDns;
    private PublicIpAddressRequest publicIpAddressRequest;

    public String getPrimaryDns() {
        return primaryDns;
    }

    public void setPrimaryDns(String primaryDns) {
        this.primaryDns = primaryDns;
    }

    public NetworkConfig primaryDns(String primaryDns) {
        setPrimaryDns(primaryDns);
        return this;
    }

    public String getSecondaryDns() {
        return secondaryDns;
    }

    public void setSecondaryDns(String secondaryDns) {
        this.secondaryDns = secondaryDns;
    }

    public NetworkConfig secondaryDns(String secondaryDns) {
        setSecondaryDns(secondaryDns);
        return this;
    }

    public NetworkRef getNetwork() {
        return network;
    }

    public void setNetwork(NetworkRef network) {
        this.network = network;
    }

    public NetworkConfig network(NetworkRef network) {
        setNetwork(network);
        return this;
    }

    public PublicIpAddressRequest getPublicIpAddressRequest() {
        return publicIpAddressRequest;
    }

    public void setPublicIpAddressRequest(PublicIpAddressRequest publicIpAddressRequest) {
        this.publicIpAddressRequest = publicIpAddressRequest;
    }

    public NetworkConfig publicIpAddress(PublicIpAddressRequest publicIpAddressRequest) {
        setPublicIpAddressRequest(publicIpAddressRequest);
        return this;
    }
}
