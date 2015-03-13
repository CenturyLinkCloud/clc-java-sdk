package com.centurylink.cloud.sdk.servers.service;

import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.domain.Template;
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

    public Template resolve(String dataCenterId, Template template) {
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

}
