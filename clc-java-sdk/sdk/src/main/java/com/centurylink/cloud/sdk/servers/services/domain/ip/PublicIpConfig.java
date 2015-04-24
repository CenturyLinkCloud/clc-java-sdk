package com.centurylink.cloud.sdk.servers.services.domain.ip;


import com.centurylink.cloud.sdk.core.services.function.Streams;
import com.centurylink.cloud.sdk.servers.services.domain.ip.port.PortConfig;
import com.centurylink.cloud.sdk.servers.services.domain.ip.port.SinglePortConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.centurylink.cloud.sdk.core.services.function.Streams.map;
import static java.util.Arrays.asList;

/**
 * @author Ilya Drabenia
 */
public class PublicIpConfig {
    private List<PortConfig> ports = new ArrayList<>();
    private List<Subnet> restrictions = new ArrayList<>();
    private String internalIpAddress;

    public List<PortConfig> getPorts() {
        return ports;
    }

    public PublicIpConfig openPorts(Integer... ports) {
        this.ports.addAll(
            map(ports, SinglePortConfig::new)
        );

        return this;
    }

    public PublicIpConfig openPorts(PortConfig... ports) {
        this.ports.addAll(asList(ports));

        return this;
    }

    public List<Subnet> getRestrictions() {
        return restrictions;
    }

    public PublicIpConfig sourceRestrictions(String... restrictions) {
        this.restrictions.addAll(
            map(restrictions, Subnet::new)
        );

        return this;
    }

    public PublicIpConfig sourceRestrictions(Subnet... restrictions) {
        this.restrictions.addAll(
            asList(restrictions)
        );

        return this;
    }

    public String getInternalIpAddress() {
        return internalIpAddress;
    }

    public PublicIpConfig internalIpAddress(String internalIpAddress) {
        this.internalIpAddress = internalIpAddress;
        return this;
    }

}
