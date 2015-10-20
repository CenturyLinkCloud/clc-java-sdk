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

package com.centurylink.cloud.sdk.base.services.client;

import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.GetDataCenterListResponse;
import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.deployment.capabilities.DatacenterDeploymentCapabilitiesMetadata;
import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.cache.Cache;
import com.centurylink.cloud.sdk.core.cache.CacheLoader;
import com.centurylink.cloud.sdk.core.client.AuthenticatedSdkHttpClient;
import com.centurylink.cloud.sdk.core.client.ClcClientException;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author ilya.drabenia
 */
public class DataCentersClient extends AuthenticatedSdkHttpClient {

    private Cache<String, DataCenterMetadata> dataCenterCache = new Cache<String, DataCenterMetadata>()
        .maximumSize(20)
        .expireAfterWrite(1, TimeUnit.HOURS)
        .build(
            new CacheLoader<String, DataCenterMetadata>() {
                @Override
                public DataCenterMetadata load(String id) throws Exception {
                    return loadDataCenter(id);
                }
            }
        );

    private static final String ALL_DATACENTERS_KEY = "";

    private Cache<String, GetDataCenterListResponse> allDataCentersCache = new Cache<String, GetDataCenterListResponse>()
        .maximumSize(20)
        .expireAfterWrite(1, TimeUnit.HOURS)
        .build(
            new CacheLoader<String, GetDataCenterListResponse>() {
                @Override
                public GetDataCenterListResponse load(String id) throws Exception {
                    return loadAllDataCenters();
                }
            }
        );

    public DataCentersClient(BearerAuthentication authFilter, SdkConfiguration config) {
        super(authFilter, config);
    }

    public DatacenterDeploymentCapabilitiesMetadata getDeploymentCapabilities(String dataCenterId) {
        return
            client("/datacenters/{accountAlias}/{dataCenterId}/deploymentCapabilities")
                .resolveTemplate("dataCenterId", dataCenterId)
                .request().get(DatacenterDeploymentCapabilitiesMetadata.class);
    }

    public DataCenterMetadata getDataCenter(String dataCenterId) {
        try {
            return dataCenterCache.get(dataCenterId);
        } catch (ExecutionException ex) {
            throw new ClcClientException(ex);
        }
    }

    private DataCenterMetadata loadDataCenter(String dataCenterId) {
        return
        client("/datacenters/{accountAlias}/{dataCenterId}")
            .queryParam("groupLinks", true)
            .resolveTemplate("dataCenterId", dataCenterId)
            .request().get(DataCenterMetadata.class);
    }

    public GetDataCenterListResponse findAllDataCenters() {
        try {
            return allDataCentersCache.get(ALL_DATACENTERS_KEY);
        } catch (ExecutionException e) {
            throw new ClcClientException(e);
        }
    }

    private GetDataCenterListResponse loadAllDataCenters() {
        return
            client("/datacenters/{accountAlias}?groupLinks=true")
                .request()
                .get(GetDataCenterListResponse.class);
    }

}
