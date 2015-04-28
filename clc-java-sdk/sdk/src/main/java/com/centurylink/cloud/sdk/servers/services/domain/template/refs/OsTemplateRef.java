package com.centurylink.cloud.sdk.servers.services.domain.template.refs;

import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.CpuArchitecture;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsType;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsFilter;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.TemplateFilter;

/**
 * @author ilya.drabenia
 */
public class OsTemplateRef extends TemplateRef {
    private final String type;
    private final CpuArchitecture architecture;
    private final String edition;
    private final String version;

    public OsTemplateRef(DataCenter dataCenter, String type, CpuArchitecture architecture, String edition,
                         String version) {
        super(dataCenter);
        this.type = type;
        this.architecture = architecture;
        this.edition = edition;
        this.version = version;
    }

    public OsTemplateRef type(String type) {
        return new OsTemplateRef(getDataCenter(), type, architecture, edition, version);
    }

    public OsTemplateRef type(OsType type) {
        return new OsTemplateRef(getDataCenter(), type.getCode(), architecture, edition, version);
    }

    public String getType() {
        return type;
    }

    public OsTemplateRef architecture(CpuArchitecture architecture) {
        return new OsTemplateRef(getDataCenter(), type, architecture, edition, version);
    }

    public CpuArchitecture getArchitecture() {
        return architecture;
    }

    public OsTemplateRef version(String version) {
        return new OsTemplateRef(getDataCenter(), type, architecture, edition, version);
    }

    public String getVersion() {
        return version;
    }

    public String getEdition() {
        return edition;
    }

    public OsTemplateRef edition(String edition) {
        return new OsTemplateRef(getDataCenter(), type, architecture, edition, version);
    }

    public OsTemplateRef dataCenter(DataCenter dataCenter) {
        return new OsTemplateRef(dataCenter, type, architecture, edition, version);
    }

    @Override
    public TemplateFilter asFilter() {
        return
            new TemplateFilter()
                .dataCenters(getDataCenter())
                .osTypes(new OsFilter()
                    .type(type != null ? type : "")
                    .architecture(architecture)
                    .version(version != null ? version : "")
                    .edition(edition != null ? edition : "")
                );
    }
}
