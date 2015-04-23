package com.centurylink.cloud.sdk.servers.services.domain.ip;

import org.apache.commons.net.util.SubnetUtils;

/**
 * @author Ilya Drabenia
 */
public class Subnet {
    private String cidr;
    private String ipAddress;
    private String mask;
    private String cidrMask;

    // used with next method to define network in format as 192.168.2.0 with mask 255.255.128.0
    public Subnet ipAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public Subnet mask(String mask) {
        this.mask = mask;
        return this;
    }

    // define network in cidr format 192.168.2.0/17
    public Subnet cidr(String cidr) {
        this.cidr = cidr;
        return this;
    }

    // for example /17 - used with ipAddress()
    public Subnet cidrMask(String cidrMask) {
        this.cidrMask = cidrMask;
        return this;
    }

    /**
     * Returns IP address in CIDR format.
     *
     * If specified {@code cidr} - returns {@code cidr} value.
     * If specified {@code ipAddress} and {@code mask} - returns calculated IP address.
     * If specified {@code ipAddress} and {@code cidrMask} - returns calculated IP address.
     * Otherwise returns {@value null}
     *
     * @return ip address string representation in CIDR format
     */
    public String getCidr() {
        if (cidr != null) {
            return cidr;
        }
        if (ipAddress != null) {
            if (mask != null) {
                return new SubnetUtils(ipAddress, mask).getInfo().getCidrSignature();
            }
            if (cidrMask != null) {
                return new SubnetUtils(ipAddress+cidrMask).getInfo().getCidrSignature();
            }
        }
        return null;
    }
}
