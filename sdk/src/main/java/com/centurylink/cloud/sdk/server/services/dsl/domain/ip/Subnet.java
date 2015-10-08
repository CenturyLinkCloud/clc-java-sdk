/*
 * (c) 2015 CenturyLink. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.server.services.dsl.domain.ip;

/**
 * @author Ilya Drabenia
 */
public class Subnet {
    private String cidr;
    private String ipAddress;
    private String mask;
    private String cidrMask;

    public Subnet() {
    }

    public Subnet(String cidr) {
        this.cidr = cidr;
    }

    public Subnet(String ipAddress, String mask) {
        this.ipAddress = ipAddress;
        this.mask = mask;
    }

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
     * Otherwise returns {@code null}
     *
     * @return ip address string representation in CIDR format
     */
    public String getCidr() {
        if (cidr != null) {
            return cidr;
        }
        if (ipAddress != null) {
            if (mask != null) {
                return new SubnetUtils(ipAddress, mask).getCidrSignature();
            }
            if (cidrMask != null) {
                return new SubnetUtils(ipAddress+cidrMask).getCidrSignature();
            }
        }
        return null;
    }
}
