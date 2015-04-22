package com.centurylink.cloud.sdk.servers.services.domain.ip.port;

/**
 * @author Ilya Drabenia
 */
public class PortRangeConfig extends PortConfig {

    public PortRangeConfig to(Integer port) {
        return new PortRangeConfig(/*this.protocol, this.from, this.to*/);
    }

}
