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

package com.centurylink.cloud.sdk.servers.services.domain.os;

import com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.CpuArchitecture;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsType;

/**
 * @author ilya.drabenia
 */
public class OperatingSystem {
    private String type;
    private CpuArchitecture architecture;
    private String edition;
    private String version;

    public OperatingSystem type(String type) {
        this.type = type;
        return this;
    }

    public OperatingSystem type(OsType type) {
        this.type = type.getCode();
        return this;
    }

    public String getType() {
        return type;
    }

    public OperatingSystem architecture(CpuArchitecture architecture) {
        this.architecture = architecture;
        return this;
    }

    public CpuArchitecture getArchitecture() {
        return architecture;
    }

    public OperatingSystem version(String version) {
        this.version = version;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public OperatingSystem edition(String edition) {
        setEdition(edition);
        return this;
    }
}
