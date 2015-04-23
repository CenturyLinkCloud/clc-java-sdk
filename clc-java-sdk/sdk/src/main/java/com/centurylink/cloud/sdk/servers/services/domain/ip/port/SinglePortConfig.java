package com.centurylink.cloud.sdk.servers.services.domain.ip.port;

import com.centurylink.cloud.sdk.servers.services.domain.ip.ProtocolType;

/**
 * @author Ilya Drabenia
 */
public class SinglePortConfig extends PortConfig {

    public SinglePortConfig(ProtocolType protocolType, Integer port) {
        super.protocol(protocolType);
        this.port = port;
    }
}
