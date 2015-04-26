package com.centurylink.cloud.sdk.servers.services.domain.template.filters;

import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.deployment.capabilities.TemplateMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.os.CpuArchitecture;

import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Ilya Drabenia
 */
public class OsFilter {
    private String osType;
    private CpuArchitecture architecture;
    private String version;
    private String edition;

    public OsFilter() {

    }

    public OsFilter type(String osType) {
        this.osType = checkNotNull(osType, "OS type must be not a null");

        return this;
    }

    public OsFilter architecture(CpuArchitecture architecture) {
        this.architecture = checkNotNull(architecture, "Architecture must be not a null");

        return this;
    }

    public OsFilter version(String version) {
        this.version = checkNotNull(version, "Version must be not a null");

        return this;
    }

    public OsFilter edition(String edition) {
        this.edition = checkNotNull(edition, "Edition must be not a null");

        return this;
    }

    public Predicate<TemplateMetadata> getPredicate() {
        return t -> {
            String osDescription = t.getOsType();

            if (osType != null) {
                if (osDescription.startsWith(osType)) {
                    osDescription = osDescription.replace(osType, "");
                } else {
                    return false;
                }
            }

            if (architecture != null) {
                if (osDescription.endsWith(architecture.getCode())) {
                    osDescription = osDescription.replace(architecture.getCode(), "");
                } else {
                    return false;
                }
            }

            if (version != null) {
                if (osDescription.startsWith(version)) {
                    osDescription = osDescription.replace(version, "");
                } else {
                    return false;
                }
            }

            if (edition != null) {
                if (osDescription.startsWith(edition)) {
                    osDescription = osDescription.replace(edition, "");
                } else {
                    return false;
                }
            }

            return true;
        };
    }
}
