/*
 * (c) 2015 CenturyLink. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.server.services.dsl;

import com.centurylink.cloud.sdk.base.services.client.DataCentersClient;
import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.deployment.capabilities.TemplateMetadata;
import com.centurylink.cloud.sdk.base.services.dsl.DataCenterService;
import com.centurylink.cloud.sdk.core.services.QueryService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.TemplateFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.refs.Template;

import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.preconditions.Preconditions.checkNotNull;

/**
 * @author ilya.drabenia
 */
public class TemplateService implements QueryService<Template, TemplateFilter, TemplateMetadata> {
    private final DataCenterService dataCenterService;
    private final DataCentersClient dataCentersClient;

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

}
