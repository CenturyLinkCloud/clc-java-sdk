package com.centurylink.cloud.sdk.servers.services.domain.ip;

import com.centurylink.cloud.sdk.servers.client.domain.ip.CreatePublicIpRequest;
import com.centurylink.cloud.sdk.servers.client.domain.ip.PortConfig;
import com.centurylink.cloud.sdk.servers.services.domain.ip.port.PortRangeConfig;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author Aliaksandr Krasitski
 */
public class PublicIpConverter {
    public CreatePublicIpRequest createPublicIpRequest(PublicIpConfig publicIpConfig) {
        List<PortConfig> ports = new ArrayList<>(publicIpConfig.getPorts().size());
        publicIpConfig.getPorts().stream()
            .forEach(port ->
                            ports.add(new PortConfig()
                                    .protocol(port.getProtocolType().name())
                                    .port(port.getPort())
                                    .portTo(port instanceof PortRangeConfig ? ((PortRangeConfig) port).getPortTo() : null))
            );


        return new CreatePublicIpRequest()
                .internalIPAddress(publicIpConfig.getInternalIpAddress())
                .sourceRestrictions(publicIpConfig.getRestrictions().stream()
                        .map(Subnet::getCidr)
                        .collect(toList()))
                .ports(ports);
    }
}
