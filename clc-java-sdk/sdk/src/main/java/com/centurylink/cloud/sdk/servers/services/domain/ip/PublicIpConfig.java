package com.centurylink.cloud.sdk.servers.services.domain.ip;


import com.centurylink.cloud.sdk.servers.services.domain.ip.port.PortConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ilya Drabenia
 */
public class PublicIpConfig {
    private List<PortConfig> ports;
    private List<Subnet> restrictions;
    private String internalIpAddress;

    public List<PortConfig> getPorts() {
        return ports;
    }

    public PublicIpConfig openPorts(Integer... ports) {
        List<PortConfig> portConfigs = new ArrayList<>(ports.length);
        Arrays.asList(ports).forEach(portNumber -> portConfigs.add(new PortConfig().port(portNumber)));

        this.ports = portConfigs;
        return this;
    }

    public PublicIpConfig openPorts(PortConfig... ports) {
        this.ports = Arrays.asList(ports);
        return this;
    }

    public List<Subnet> getRestrictions() {
        return restrictions;
    }

    public PublicIpConfig sourceRestrictions(String... restrictions) {
        List<Subnet> subnets = new ArrayList<>(restrictions.length);
        Arrays.asList(restrictions).forEach(restriction -> subnets.add(new Subnet().cidr(restriction)));

        this.restrictions = subnets;

        return this;
    }

    public PublicIpConfig sourceRestrictions(Subnet... restrictions) {
        this.restrictions = Arrays.asList(restrictions);
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
