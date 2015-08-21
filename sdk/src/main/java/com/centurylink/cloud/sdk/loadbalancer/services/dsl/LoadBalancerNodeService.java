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

package com.centurylink.cloud.sdk.loadbalancer.services.dsl;

import com.centurylink.cloud.sdk.base.services.dsl.DataCenterService;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.NoWaitingJobFuture;
import com.centurylink.cloud.sdk.core.services.QueryService;
import com.centurylink.cloud.sdk.loadbalancer.services.client.LoadBalancerNodeClient;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerNodeMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerPoolMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.filter.LoadBalancerNodeFilter;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.group.LoadBalancer;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.node.LoadBalancerNode;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.pool.LoadBalancerPool;
import com.google.inject.Inject;

import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoadBalancerNodeService implements QueryService<LoadBalancerNode, LoadBalancerNodeFilter, LoadBalancerNodeMetadata> {

    private final LoadBalancerNodeClient loadBalancerNodeClient;
    private final DataCenterService dataCenterService;
    private final LoadBalancerService loadBalancerService;
    private final LoadBalancerPoolService loadBalancerPoolService;

    @Inject
    public LoadBalancerNodeService(
            LoadBalancerNodeClient loadBalancerNodeClient,
            LoadBalancerService loadBalancerService,
            LoadBalancerPoolService loadBalancerPoolService,
            DataCenterService dataCenterService
    ) {
        this.loadBalancerNodeClient = loadBalancerNodeClient;
        this.loadBalancerService = loadBalancerService;
        this.loadBalancerPoolService = loadBalancerPoolService;
        this.dataCenterService = dataCenterService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<LoadBalancerNodeMetadata> findLazy(LoadBalancerNodeFilter filter) {
        checkNotNull(filter, "Filter must be not a null");

        Stream<LoadBalancerPoolMetadata> loadBalancerPools = loadBalancerPoolService.findLazy(
                filter.getLoadBalancerPoolFilter()
        );

        return
            loadBalancerPools
                .flatMap(loadBalancerPoolMetadata -> loadBalancerNodeClient
                    .getLoadBalancerNodes(
                            loadBalancerPoolMetadata.getDataCenterId(),
                            loadBalancerPoolMetadata.getLoadBalancerId(),
                            loadBalancerPoolMetadata.getId()
                    )
                    .stream()
                )
                .filter(filter.getPredicate());
    }

    public OperationFuture<LoadBalancerPool> update(
            LoadBalancerPool loadBalancerPool,
            List<LoadBalancerNodeMetadata> nodesList
    ) {
        LoadBalancer loadBalancer = loadBalancerPool.getLoadBalancer();

        loadBalancerNodeClient.update(
                fetchDataCenterId(loadBalancer.getDataCenter()),
                fetchLoadBalancerId(loadBalancer),
                fetchLoadBalancerPoolId(loadBalancerPool),
                nodesList
        );

        return new OperationFuture<>(
                loadBalancerPool,
                new NoWaitingJobFuture()
        );
    }

    private String fetchDataCenterId(DataCenter dataCenter) {
        return dataCenterService.findByRef(dataCenter).getId();
    }

    private String fetchLoadBalancerId(LoadBalancer loadBalancer) {
        return loadBalancerService.findByRef(loadBalancer).getId();
    }

    private String fetchLoadBalancerPoolId(LoadBalancerPool loadBalancerPool) {
        return loadBalancerPoolService.findByRef(loadBalancerPool).getId();
    }

}
