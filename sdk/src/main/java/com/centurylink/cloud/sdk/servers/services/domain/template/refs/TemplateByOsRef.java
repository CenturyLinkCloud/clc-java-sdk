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

package com.centurylink.cloud.sdk.servers.services.domain.template.refs;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.TemplateFilter;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.CpuArchitecture;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsFilter;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsType;

import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.CpuArchitecture.x86_64;
import static com.google.common.base.Strings.nullToEmpty;

/**
 * @author ilya.drabenia
 */
public class TemplateByOsRef extends Template {
    private final String type;
    private final CpuArchitecture architecture;
    private final String edition;
    private final String version;

    TemplateByOsRef(DataCenter dataCenter, String type, CpuArchitecture architecture, String edition,
                           String version) {
        super(dataCenter);
        this.type = type;
        this.architecture = architecture;
        this.edition = edition;
        this.version = version;
    }

    public TemplateByOsRef type(String type) {
        return new TemplateByOsRef(getDataCenter(), type, architecture, edition, version);
    }

    public TemplateByOsRef type(OsType type) {
        return new TemplateByOsRef(getDataCenter(), type.getCode(), architecture, edition, version);
    }

    public String getType() {
        return type;
    }

    public TemplateByOsRef architecture(CpuArchitecture architecture) {
        return new TemplateByOsRef(getDataCenter(), type, architecture, edition, version);
    }

    public CpuArchitecture getArchitecture() {
        return architecture;
    }

    public TemplateByOsRef version(String version) {
        return new TemplateByOsRef(getDataCenter(), type, architecture, edition, version);
    }

    public String getVersion() {
        return version;
    }

    public String getEdition() {
        return edition;
    }

    public TemplateByOsRef edition(String edition) {
        return new TemplateByOsRef(getDataCenter(), type, architecture, edition, version);
    }

    public TemplateByOsRef dataCenter(DataCenter dataCenter) {
        return new TemplateByOsRef(dataCenter, type, architecture, edition, version);
    }

    @Override
    public TemplateFilter asFilter() {
        return
            new TemplateFilter()
                .dataCenters(getDataCenter())
                .osTypes(new OsFilter()
                    .type(nullToEmpty(type))
                    .architecture(architecture != null ? architecture : x86_64)
                    .version(nullToEmpty(version))
                    .edition(nullToEmpty(edition))
                );
    }
}
