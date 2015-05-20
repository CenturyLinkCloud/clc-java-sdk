package com.centurylink.cloud.sdk.servers.services.domain.ip.port;

import com.centurylink.cloud.sdk.servers.services.domain.ip.ProtocolType;

import static com.centurylink.cloud.sdk.servers.services.domain.ip.ProtocolType.TCP;

/**
 * @author Ilya Drabenia
 */
public class SinglePortConfig extends PortConfig {

    public SinglePortConfig(Integer port) {
        this(TCP, port);
    }

    public SinglePortConfig(ProtocolType protocolType, Integer port) {
        super.protocol(protocolType);
        this.port = port;
    }
}
