package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.services.domain.datacenter.DataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.datacenter.DataCenters;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import com.centurylink.cloud.sdk.servers.services.domain.template.TemplateConverter;
import com.google.inject.Inject;

import java.util.List;

/**
 * @author ilya.drabenia
 */
public class TemplateService {
    private final ServerClient serversClient;
    private final TemplateConverter converter;

    @Inject
    public TemplateService(ServerClient serversClient, TemplateConverter converter) {
        this.serversClient = serversClient;
        this.converter = converter;
    }

    public Template resolveName(String dataCenterId, Template template) {
        if (template.getName() != null) {
            return template;
        } else {
            return resolveByOs(dataCenterId, template);
        }
    }

    public Template resolveByOs(String dataCenterId, Template template) {
        return
            template
                .name(
                    serversClient
                        .getDataCenterDeploymentCapabilities(dataCenterId)
                        .findByOsType(template.getOs())
                        .getName()
                );
    }

    public List<Template> findByDataCenter(String dataCenter) {
        return converter.templateListFrom(
            serversClient
                .getDataCenterDeploymentCapabilities(dataCenter)
                .getTemplates()
        );
    }

    public List<Template> findByDataCenter(DataCenter dataCenter) {
        return converter.templateListFrom(
            serversClient
                .getDataCenterDeploymentCapabilities(dataCenter.getId())
                .getTemplates()
        );
    }

}
