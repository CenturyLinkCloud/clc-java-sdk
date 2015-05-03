package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.commons.client.DataCentersClient;
import com.centurylink.cloud.sdk.commons.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.commons.client.domain.datacenters.deployment.capabilities.TemplateMetadata;
import com.centurylink.cloud.sdk.commons.services.DataCenterService;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.TemplateFilter;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.Template;
import com.google.inject.Inject;

import java.util.List;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.base.services.refs.Reference.notFound;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * @author ilya.drabenia
 */
public class TemplateService {
    private final DataCenterService dataCenterService;
    private final DataCentersClient dataCentersClient;

    @Inject
    public TemplateService(DataCenterService dataCenterService, DataCentersClient dataCentersClient) {
        this.dataCenterService = dataCenterService;
        this.dataCentersClient = dataCentersClient;
    }

    public TemplateMetadata findByRef(Template templateRef) {
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
            filter.applyFindLazy(criteria ->
                dataCenterService
                    .findLazy(criteria.getDataCenter())
                    .map(DataCenterMetadata::getId)
                    .map(dataCentersClient::getDeploymentCapabilities)
                    .flatMap(c -> c.getTemplates().stream())
                    .filter(criteria.getPredicate())
            );
    }

    public List<TemplateMetadata> findByDataCenter(String dataCenterId) {
        return
            dataCentersClient
                .getDeploymentCapabilities(dataCenterId)
                .getTemplates();
    }

}
