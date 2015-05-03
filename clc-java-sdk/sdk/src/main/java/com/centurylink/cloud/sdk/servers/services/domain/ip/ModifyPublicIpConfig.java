package com.centurylink.cloud.sdk.servers.services.domain.ip;


import com.centurylink.cloud.sdk.servers.services.domain.ip.port.PortConfig;
import com.centurylink.cloud.sdk.servers.services.domain.ip.port.SinglePortConfig;

import java.util.ArrayList;
import java.util.List;

import static com.centurylink.cloud.sdk.base.function.Streams.map;
import static java.util.Arrays.asList;

/**
 * @author Ilya Drabenia
 */
public class ModifyPublicIpConfig {
    private List<PortConfig> ports = new ArrayList<>();
    private List<Subnet> restrictions = new ArrayList<>();

    public List<PortConfig> getPorts() {
        return ports;
    }

    public ModifyPublicIpConfig openPorts(Integer... ports) {
        this.ports.addAll(
            map(ports, SinglePortConfig::new)
        );

        return this;
    }

    public ModifyPublicIpConfig openPorts(PortConfig... ports) {
        this.ports.addAll(asList(ports));

        return this;
    }

    public List<Subnet> getRestrictions() {
        return restrictions;
    }

    public ModifyPublicIpConfig sourceRestrictions(String... restrictions) {
        this.restrictions.addAll(
            map(restrictions, Subnet::new)
        );

        return this;
    }

    public ModifyPublicIpConfig sourceRestrictions(Subnet... restrictions) {
        this.restrictions.addAll(
            asList(restrictions)
        );

        return this;
    }
}
