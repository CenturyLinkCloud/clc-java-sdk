package com.centurylink.cloud.sdk.servers.services.domain.ip.port;

import com.centurylink.cloud.sdk.servers.services.domain.ip.ProtocolType;

/**
 * @author Ilya Drabenia
 */
public class PortRangeConfig extends PortConfig {
    private Integer portTo;

    public PortRangeConfig(ProtocolType protocolType, Integer from, Integer to) {
        super.protocol(protocolType);
        this.port = from;
        this.portTo = to;
    }

    public Integer getPortTo() {
        return portTo;
    }

}
