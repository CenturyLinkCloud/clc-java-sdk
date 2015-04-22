package com.centurylink.cloud.sdk.servers.services.domain.ip;

/**
 * @author Ilya Drabenia
 */
public class Subnet {

    // used with next method to define network in format as 192.168.2.0 with mask 255.255.128.0
    public Subnet ipAddress(String ipAddress) {
        return this;
    }

    public Subnet mask(String mask) {
        return this;
    }

    // define network in cidr format 192.168.2.0/17
    public Subnet cidr(String cidr) {
        return this;
    }

    // for example /17 - used with ipAddress()
    public Subnet cidrMask(String cidrMask) {
        return this;
    }

}
