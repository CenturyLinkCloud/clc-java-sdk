package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.common.management.client.DataCentersClient;
import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.deployment.capabilities.TemplateMetadata;
import com.centurylink.cloud.sdk.common.management.services.DataCenterService;
import com.centurylink.cloud.sdk.core.services.QueryService;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.TemplateFilter;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.Template;
import com.google.inject.Inject;

import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author ilya.drabenia
 */
public class TemplateService implements QueryService<Template, TemplateFilter, TemplateMetadata> {
    private final DataCenterService dataCenterService;
    private final DataCentersClient dataCentersClient;

    @Inject
    public TemplateService(DataCenterService dataCenterService, DataCentersClient dataCentersClient) {
        this.dataCenterService = dataCenterService;
        this.dataCentersClient = dataCentersClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
