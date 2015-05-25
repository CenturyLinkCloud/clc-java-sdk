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

package com.centurylink.cloud.sdk.common.management.services;

import com.centurylink.cloud.sdk.common.management.client.DataCentersClient;
import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.deployment.capabilities.DatacenterDeploymentCapabilitiesMetadata;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenterByIdRef;
import com.centurylink.cloud.sdk.core.services.QueryService;
import com.google.inject.Inject;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author ilya.drabenia
 */
public class DataCenterService implements QueryService<DataCenter, DataCenterFilter, DataCenterMetadata> {
    private final DataCentersClient serverClient;

    @Inject
    public DataCenterService(DataCentersClient serverClient) {
        this.serverClient = serverClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<DataCenterMetadata> findLazy(DataCenterFilter criteria) {
        return findAll().stream().filter(criteria.getPredicate());
    }

    public List<DataCenterMetadata> findAll() {
        return serverClient.findAllDataCenters();
    }

    public DatacenterDeploymentCapabilitiesMetadata getDeploymentCapabilities(DataCenter ref) {
        return serverClient.getDeploymentCapabilities(idByRef(ref));
    }

    String idByRef(DataCenter ref) {
        if (ref.is(DataCenterByIdRef.class)) {
            return ref.as(DataCenterByIdRef.class).getId();
        } else {
            return findByRef(ref).getId();
        }
    }
}
