package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.server.CreateServerResponse;
import com.centurylink.cloud.sdk.servers.services.domain.Response;
import com.centurylink.cloud.sdk.servers.services.domain.datacenter.DataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.datacenter.refs.DataCenterRef;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import com.centurylink.cloud.sdk.servers.services.domain.template.TemplateConverter;
import com.google.inject.Inject;

import java.util.List;

/**
 * @author ilya.drabenia
 */
public class TemplateService {
    private final DataCenterService dataCenterService;
    private final ServerClient serversClient;
    private final TemplateConverter converter;

    @Inject
    public TemplateService(ServerClient serversClient, TemplateConverter converter, DataCenterService dataCenterService) {
        this.serversClient = serversClient;
        this.converter = converter;
        this.dataCenterService = dataCenterService;
    }

    public Template resolveName(DataCenterRef dataCenter, Template template) {
        String dataCenterId = dataCenterService.resolveRef(dataCenter).getId();

        if (template.getName() != null) {
            return template;
        } else if (template.getDescription() != null) {
            return template.name(
                serversClient
                    .getDataCenterDeploymentCapabilities(dataCenterId)
                    .findByName(template.getDescription())
                    .getName()
            );
        } else {
            return resolveByOs(
                dataCenterService.resolveRef(dataCenter).getId(),
                template
            );
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

    public Response<Template> delete(Template template) {
        CreateServerResponse response = serversClient.delete(template.getName());

        return new Response<>(
            template,
            response.findStatusId(),
            serversClient
        );
    }

}
