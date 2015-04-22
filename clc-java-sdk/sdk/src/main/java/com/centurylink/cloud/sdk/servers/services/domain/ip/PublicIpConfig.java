package com.centurylink.cloud.sdk.servers.services.domain.ip;

import com.centurylink.cloud.sdk.servers.client.domain.ip.PortConfig;

/**
 * @author Ilya Drabenia
 */
public class PublicIpConfig {

    public PublicIpConfig ports(Integer... ports) {
        return this;
    }

    public PublicIpConfig ports(PortConfig... ports) {
        return this;
    }

    public PublicIpConfig sourceRestrictions(String... restrictions) {
        return this;
    }

}
