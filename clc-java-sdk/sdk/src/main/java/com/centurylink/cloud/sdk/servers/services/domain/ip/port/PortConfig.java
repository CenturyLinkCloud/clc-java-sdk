package com.centurylink.cloud.sdk.servers.services.domain.ip.port;

import com.centurylink.cloud.sdk.servers.services.domain.ip.ProtocolType;
import com.google.common.base.Preconditions;

/**
 * @author Ilya Drabenia
 */
public class PortConfig {
    public static final Integer HTTP = 80;
    public static final Integer HTTPS = 443;
    public static final Integer SSH = 22;
    public static final Integer RDP = 3389; //
    public static final Integer FTP = 21; //

    private ProtocolType protocolType = ProtocolType.TCP;
    protected Integer port;

    private static final int MIN_PORT = 0;
    private static final int MAX_PORT = 65535;

    public PortRangeConfig to(Integer to) {
        Preconditions.checkArgument(to > MIN_PORT && to < MAX_PORT && this.port < to);
        return new PortRangeConfig(this.protocolType, this.port, to);
    }

    public Integer getPort() {
        return port;
    }

    public SinglePortConfig port(Integer port) {
        Preconditions.checkArgument(port > MIN_PORT && port < MAX_PORT);

        return new SinglePortConfig(protocolType, port);
    }

    public ProtocolType getProtocolType() {
        return protocolType;
    }

    // default is TCP
    public PortConfig protocol(ProtocolType protocolType) {
        this.protocolType = protocolType;
        return this;
    }

}
