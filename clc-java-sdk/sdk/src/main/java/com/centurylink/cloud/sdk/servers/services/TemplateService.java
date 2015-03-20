package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.core.datacenters.client.DataCentersClient;
import com.centurylink.cloud.sdk.core.datacenters.services.DataCenterService;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.core.datacenters.client.domain.deployment.capacilities.GetDeploymentCapabilitiesResponse;
import com.centurylink.cloud.sdk.core.datacenters.client.domain.deployment.capacilities.TemplateMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.CreateServerResponse;
import com.centurylink.cloud.sdk.servers.services.domain.Response;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.datacenter.refs.DataCenterRef;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import com.centurylink.cloud.sdk.servers.services.domain.template.TemplateConverter;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.DescriptionTemplateRef;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.NameTemplateRef;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.OsTemplateRef;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.TemplateRef;
import com.google.inject.Inject;

import java.util.List;

/**
 * @author ilya.drabenia
 */
public class TemplateService {
    private final DataCenterService dataCenterService;
    private final ServerClient serversClient;
    private final DataCentersClient dataCentersClient;
    private final TemplateConverter converter;

    @Inject
    public TemplateService(DataCenterService dataCenterService, ServerClient serversClient,
                           DataCentersClient dataCentersClient, TemplateConverter converter) {
        this.dataCenterService = dataCenterService;
        this.serversClient = serversClient;
        this.dataCentersClient = dataCentersClient;
        this.converter = converter;
    }

    public TemplateMetadata findByRef(TemplateRef template) {
        GetDeploymentCapabilitiesResponse deploymentCapabilities =
            dataCentersClient
                .getDataCenterDeploymentCapabilities(
                    dataCenterService.findByRef(template.getDataCenter()).getId()
                );

        if (template.is(NameTemplateRef.class)) {
            return
                deploymentCapabilities
                    .findByName(
                        template.as(NameTemplateRef.class).getName()
                    );
        } else if (template.is(DescriptionTemplateRef.class)) {
            return
                deploymentCapabilities
                    .findByDescription(
                        template.as(DescriptionTemplateRef.class).getDescription()
                    );
        } else {
            return deploymentCapabilities.findByOsType(template.as(OsTemplateRef.class));
        }
    }

    public List<Template> findByDataCenter(String dataCenterId) {
        return converter.templateListFrom(
            dataCentersClient
                .getDataCenterDeploymentCapabilities(dataCenterId)
                .getTemplates()
        );
    }

    public List<Template> findByDataCenter(DataCenterRef dataCenter) {
        return converter.templateListFrom(
            dataCentersClient
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
