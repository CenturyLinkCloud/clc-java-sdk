package com.centurylink.cloud.sdk.servers.services.domain.ip;


import com.centurylink.cloud.sdk.servers.services.domain.ip.port.PortConfig;
import com.centurylink.cloud.sdk.servers.services.domain.ip.port.SinglePortConfig;

import java.util.ArrayList;
import java.util.List;

import static com.centurylink.cloud.sdk.core.services.function.Streams.map;
import static java.util.Arrays.asList;

/**
 * @author Ilya Drabenia
 */
public class CreatePublicIpConfig {
    private List<PortConfig> ports = new ArrayList<>();
    private List<Subnet> restrictions = new ArrayList<>();
    private String internalIpAddress;

    public List<PortConfig> getPorts() {
        return ports;
    }

    public CreatePublicIpConfig openPorts(Integer... ports) {
        this.ports.addAll(
            map(ports, SinglePortConfig::new)
        );

        return this;
    }

    public CreatePublicIpConfig openPorts(PortConfig... ports) {
        this.ports.addAll(asList(ports));

        return this;
    }

    public List<Subnet> getRestrictions() {
        return restrictions;
    }

    public CreatePublicIpConfig sourceRestrictions(String... restrictions) {
        this.restrictions.addAll(
            map(restrictions, Subnet::new)
        );

        return this;
    }

    public CreatePublicIpConfig sourceRestrictions(Subnet... restrictions) {
        this.restrictions.addAll(
            asList(restrictions)
        );

        return this;
    }

    public String getInternalIpAddress() {
        return internalIpAddress;
    }

    public CreatePublicIpConfig internalIpAddress(String internalIpAddress) {
        this.internalIpAddress = internalIpAddress;
        return this;
    }

}
