package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.core.commons.client.DataCentersClient;
import com.centurylink.cloud.sdk.core.commons.client.QueueClient;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.deployment.capabilities.TemplateMetadata;
import com.centurylink.cloud.sdk.core.commons.services.DataCenterService;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.server.BaseServerResponse;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.OperationFuture;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import com.centurylink.cloud.sdk.servers.services.domain.template.TemplateConverter;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.TemplateFilter;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.TemplateRef;
import com.google.inject.Inject;

import java.util.List;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.services.refs.Reference.notFound;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * @author ilya.drabenia
 */
public class TemplateService {
    private final DataCenterService dataCenterService;
    private final ServerClient serversClient;
    private final DataCentersClient dataCentersClient;
    private final TemplateConverter converter;
    private final QueueClient queueClient;

    @Inject
    public TemplateService(DataCenterService dataCenterService, ServerClient serversClient,
                           DataCentersClient dataCentersClient, TemplateConverter converter,
                           QueueClient queueClient) {
        this.dataCenterService = dataCenterService;
        this.serversClient = serversClient;
        this.dataCentersClient = dataCentersClient;
        this.converter = converter;
        this.queueClient = queueClient;
    }

    public TemplateMetadata findByRef(TemplateRef templateRef) {
        checkNotNull(templateRef, "Reference must be not a null");

        return
            findLazy(templateRef.asFilter())
                .findFirst()
                .orElseThrow(notFound(templateRef));
    }

    public List<TemplateMetadata> find(TemplateFilter filter) {
        checkNotNull(filter, "Filter must be not a null");

        return findLazy(filter).collect(toList());
    }

    public Stream<TemplateMetadata> findLazy(TemplateFilter filter) {
        checkNotNull(filter, "Filter must be not a null");

        return
            dataCenterService
                .findLazy(filter.getDataCenter())
                .map(DataCenterMetadata::getId)
                .map(dataCentersClient::getDataCenterDeploymentCapabilities)
                .flatMap(c -> c.getTemplates().stream())
                .filter(filter.getPredicate());
    }

    public List<Template> findByDataCenter(String dataCenterId) {
        return converter.templateListFrom(
            dataCentersClient
                .getDataCenterDeploymentCapabilities(dataCenterId)
                .getTemplates()
        );
    }

    public OperationFuture<Template> delete(Template template) {
        BaseServerResponse response = serversClient.delete(template.getName());

        return new OperationFuture<>(
            template,
            response.findStatusId(),
            queueClient
        );
    }

}
