package com.centurylink.cloud.sdk.servers.services.domain.ip;

import com.centurylink.cloud.sdk.servers.client.domain.ip.PortConfig;
import com.centurylink.cloud.sdk.servers.client.domain.ip.PublicIpRequest;
import com.centurylink.cloud.sdk.servers.services.domain.ip.port.PortRangeConfig;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author Aliaksandr Krasitski
 */
public class PublicIpConverter {
    public PublicIpRequest createPublicIpRequest(PublicIpConfig publicIpConfig) {
        List<PortConfig> ports = new ArrayList<>(publicIpConfig.getPorts().size());
        publicIpConfig.getPorts().stream()
            .forEach(port ->
                ports.add(new PortConfig()
                    .protocol(port.getProtocolType().name())
                    .port(port.getPort())
                    .portTo(port instanceof PortRangeConfig ? ((PortRangeConfig) port).getPortTo() : null))
            );

        return new PublicIpRequest()
            .internalIPAddress(publicIpConfig.getInternalIpAddress())
            .sourceRestrictions(publicIpConfig.getRestrictions().stream()
                .map(Subnet::getCidr)
                .collect(toList()))
            .ports(ports);
    }
}
