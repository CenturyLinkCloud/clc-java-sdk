package com.centurylinkcloud.servers.service;

import com.centurylinkcloud.servers.client.ServerClient;
import com.centurylinkcloud.servers.domain.Group;
import com.centurylinkcloud.servers.domain.Template;
import com.google.inject.Inject;

/**
 * @author ilya.drabenia
 */
public class TemplateService {
    private final ServerClient serversClient;

    @Inject
    public TemplateService(ServerClient serversClient) {
        this.serversClient = serversClient;
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

}
