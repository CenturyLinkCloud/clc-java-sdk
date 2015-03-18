package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.server.CreateServerResponse;
import com.centurylink.cloud.sdk.servers.services.domain.Response;
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
        String dataCenterId = dataCenterService.findByRef(dataCenter).getId();

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
                dataCenterService.findByRef(dataCenter).getId(),
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

    public List<Template> findByDataCenter(String dataCenterId) {
        return converter.templateListFrom(
            serversClient
                .getDataCenterDeploymentCapabilities(dataCenterId)
                .getTemplates()
        );
    }

    public List<Template> findByDataCenter(DataCenterRef dataCenter) {

        return converter.templateListFrom(
            serversClient
                .getDataCenterDeploymentCapabilities(
                    dataCenterService.findByRef(dataCenter).getId()
                )
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
