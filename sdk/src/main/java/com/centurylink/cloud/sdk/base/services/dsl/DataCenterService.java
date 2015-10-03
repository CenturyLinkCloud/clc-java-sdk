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

package com.centurylink.cloud.sdk.base.services.dsl;

import com.centurylink.cloud.sdk.base.services.client.DataCentersClient;
import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.deployment.capabilities.DatacenterDeploymentCapabilitiesMetadata;
import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.deployment.capabilities.NetworkMetadata;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenterByIdRef;
import com.centurylink.cloud.sdk.base.services.dsl.domain.networks.refs.IdNetworkRef;
import com.centurylink.cloud.sdk.base.services.dsl.domain.networks.refs.NetworkRef;
import com.centurylink.cloud.sdk.core.exceptions.ReferenceNotSupportedException;
import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.core.services.QueryService;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author ilya.drabenia
 */
public class DataCenterService implements QueryService<DataCenter, DataCenterFilter, DataCenterMetadata> {
    private final DataCentersClient client;

    @Inject
    public DataCenterService(DataCentersClient client) {
        this.client = client;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<DataCenterMetadata> findLazy(DataCenterFilter criteria) {
        return findAll().stream().filter(criteria.getPredicate());
    }

    public List<DataCenterMetadata> findAll() {
        return client.findAllDataCenters();
    }

    public DatacenterDeploymentCapabilitiesMetadata getDeploymentCapabilities(DataCenter dataCenter) {
        return client.getDeploymentCapabilities(idByRef(dataCenter));
    }

    String idByRef(DataCenter ref) {
        if (ref.is(DataCenterByIdRef.class)) {
            return ref.as(DataCenterByIdRef.class).getId();
        } else {
            return findByRef(ref).getId();
        }
    }

    public NetworkMetadata findNetworkByRef(NetworkRef networkRef) {
        if (networkRef.is(IdNetworkRef.class)) {
            return
                getDeploymentCapabilities(networkRef.getDataCenter())
                    .findNetworkById(networkRef.as(IdNetworkRef.class).getId());
        } else {
            throw new ReferenceNotSupportedException(networkRef.getClass());
        }
    }

    public List<NetworkMetadata> findNetworkByDataCenter(DataCenter dataCenter) {
        return
            client
                .getDeploymentCapabilities(idByRef(dataCenter))
                .getDeployableNetworks();
    }
}
