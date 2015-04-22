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

    public PortRangeConfig from(Integer from) {
        return new PortRangeConfig(/*protocol, from*/);
    }

    public SinglePortConfig port(Integer port) {
        Preconditions.checkArgument(port > 0 && port < 65535);

        return new SinglePortConfig(/*protocol, port*/);
    }

    // default is TCP
    public PortConfig protocol(ProtocolType protocol) {
        return this;
    }

}
