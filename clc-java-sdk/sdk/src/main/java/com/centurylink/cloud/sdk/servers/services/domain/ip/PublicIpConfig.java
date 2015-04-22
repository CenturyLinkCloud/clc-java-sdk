package com.centurylink.cloud.sdk.servers.services.domain.ip;

import com.centurylink.cloud.sdk.servers.client.domain.ip.PortConfig;

/**
 * @author Ilya Drabenia
 */
public class PublicIpConfig {

    public PublicIpConfig openPorts(Integer... ports) {
        return this;
    }

    public PublicIpConfig openPorts(PortConfig... ports) {
        return this;
    }

    public PublicIpConfig sourceRestrictions(String... restrictions) {
        return this;
    }

    public PublicIpConfig sourceRestrictions(Subnet... restrictions) {
        return this;
    }

}
