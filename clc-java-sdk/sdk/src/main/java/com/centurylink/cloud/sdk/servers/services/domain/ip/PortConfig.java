package com.centurylink.cloud.sdk.servers.services.domain.ip;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

/**
 * @author aliaksandr.krasitski
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "protocol",
        "port",
        "portTo"
})
public class PortConfig {
    private String protocol;
    private Integer port;
    private Integer portTo;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public PortConfig protocol(ProtocolType protocolType) {
        setProtocol(protocolType.name());
        return this;
    }

    public PortConfig protocol(String protocol) {
        setProtocol(protocol);
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public PortConfig port(Integer port) {
        setPort(port);
        return this;
    }

    public Integer getPortTo() {
        return portTo;
    }

    public void setPortTo(Integer portTo) {
        this.portTo = portTo;
    }

    public PortConfig portTo(Integer portTo) {
        setPortTo(portTo);
        return this;
    }
}
