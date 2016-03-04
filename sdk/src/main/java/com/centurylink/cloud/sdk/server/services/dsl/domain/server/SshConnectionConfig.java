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

package com.centurylink.cloud.sdk.server.services.dsl.domain.server;

/**
 * @author siarhei.sidarau
 */
public class SshConnectionConfig {
    private boolean useInternalIp;
    private String ip;

    public SshConnectionConfig useInternalIp() {
        setUseInternalIp(true);

        return this;
    }

    public SshConnectionConfig ip(String ip) {
        setIp(ip);

        return this;
    }

    public void setUseInternalIp(boolean useInternalIp) {
        this.useInternalIp = useInternalIp;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isUseInternalIp() {
        return useInternalIp;
    }

    public String getIp() {
        return ip;
    }
}
