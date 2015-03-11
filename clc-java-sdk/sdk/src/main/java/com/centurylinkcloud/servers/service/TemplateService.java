package com.centurylinkcloud.servers.service;

import com.centurylinkcloud.servers.client.ServerClient;
import com.centurylinkcloud.servers.domain.Group;
import com.centurylinkcloud.servers.domain.Template;
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
