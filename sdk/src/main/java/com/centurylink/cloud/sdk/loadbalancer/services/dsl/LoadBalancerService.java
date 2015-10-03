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

import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.base.services.dsl.DataCenterService;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.JobFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.NoWaitingJobFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.ParallelJobsFuture;
import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.core.services.QueryService;
import com.centurylink.cloud.sdk.loadbalancer.services.client.LoadBalancerClient;
import com.centurylink.cloud.sdk.loadbalancer.services.client.domain.LoadBalancerRequest;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerConfig;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.filter.LoadBalancerFilter;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.group.LoadBalancer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.function.Predicates.*;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

public class LoadBalancerService implements QueryService<LoadBalancer, LoadBalancerFilter, LoadBalancerMetadata> {

    private final LoadBalancerClient loadBalancerClient;
    private final DataCenterService dataCenterService;

    @Inject
    public LoadBalancerService(LoadBalancerClient loadBalancerClient, DataCenterService dataCenterService) {
        this.loadBalancerClient = loadBalancerClient;
        this.dataCenterService = dataCenterService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<LoadBalancerMetadata> findLazy(LoadBalancerFilter filter) {
        checkNotNull(filter, "Filter must be not a null");

        Stream<DataCenterMetadata> dataCenters = dataCenterService.findLazy(
                filter.getDataCenterFilter()
        );

        return
            dataCenters
                .flatMap(datacenter -> loadBalancerClient
                    .getLoadBalancers(datacenter.getId())
                    .stream()
                )
                .filter(filter.getPredicate())
                .filter(filter.getIds().size() > 0 ?
                    combine(LoadBalancerMetadata::getId, in(filter.getIds())) :
                    alwaysTrue()
                );
    }

    /**
     * Create load balancer
     *
     * @param config load balancer config
     * @return OperationFuture wrapper for load balancer
     */
    public OperationFuture<LoadBalancer> create(LoadBalancerConfig config) {
        String dataCenterId = dataCenterService.findByRef(config.getDataCenter()).getId();

        LoadBalancerMetadata loadBalancer = loadBalancerClient.create(
                dataCenterId,
                new LoadBalancerRequest()
                        .name(config.getName())
                        .description(config.getDescription())
                        .status(config.getStatus())
        );

        return new OperationFuture<>(
            LoadBalancer.refById(
                    loadBalancer.getId(),
                    DataCenter.refById(dataCenterId)
            ),
            new NoWaitingJobFuture()
        );
    }

    /**
     * Update load balancer
     *
     * @param loadBalancer load balancer
     * @param config load balancer config
     * @return OperationFuture wrapper for load balancer
     */
    public OperationFuture<LoadBalancer> update(LoadBalancer loadBalancer, LoadBalancerConfig config) {
        LoadBalancerMetadata loadBalancerMetadata = findByRef(loadBalancer);

        loadBalancerClient.update(
                loadBalancerMetadata.getDataCenterId(),
                loadBalancerMetadata.getId(),
                new LoadBalancerRequest()
                        .name(config.getName())
                        .description(config.getDescription())
                        .status(config.getStatus())
        );

        return new OperationFuture<>(
                loadBalancer,
                new NoWaitingJobFuture()
        );
    }

    /**
     * Update load balancer list
     *
     * @param loadBalancerList load balancer list
     * @param config load balancer config
     * @return OperationFuture wrapper for load balancer list
     */
    public OperationFuture<List<LoadBalancer>> update(
            List<LoadBalancer> loadBalancerList,
            LoadBalancerConfig config
    ) {
        loadBalancerList.forEach(loadBalancer -> update(loadBalancer, config));

        return new OperationFuture<>(
                loadBalancerList,
                new NoWaitingJobFuture()
        );
    }

    /**
     * Update filtered load balancers
     *
     * @param loadBalancerFilter load balancer filter
     * @param config load balancer config
     * @return OperationFuture wrapper for load balancer list
     */
    public OperationFuture<List<LoadBalancer>> update(
            LoadBalancerFilter loadBalancerFilter,
            LoadBalancerConfig config
    ) {
        checkNotNull(loadBalancerFilter, "Load balancer filter must be not null");

        List<LoadBalancer> loadBalancerList = findLazy(loadBalancerFilter)
                .map(metadata -> LoadBalancer.refById(
                    metadata.getId(),
                    DataCenter.refById(metadata.getDataCenterId())
                ))
                .collect(toList());

        return update(loadBalancerList, config);
    }

    /**
     * Delete load balancer
     *
     * @param loadBalancer load balancer
     * @return OperationFuture wrapper for load balancer
     */
    public OperationFuture<LoadBalancer> delete(LoadBalancer loadBalancer) {
        LoadBalancerMetadata loadBalancerMetadata = findByRef(loadBalancer);

        loadBalancerClient.delete(
                loadBalancerMetadata.getDataCenterId(),
                loadBalancerMetadata.getId()
        );

        return new OperationFuture<>(
                loadBalancer,
                new NoWaitingJobFuture()
        );
    }

    /**
     * Delete array of load balancers
     *
     * @param loadBalancer array of load balancer
     * @return OperationFuture wrapper for load balancer list
     */
    public OperationFuture<List<LoadBalancer>> delete(LoadBalancer... loadBalancer) {
        return delete(Arrays.asList(loadBalancer));
    }

    /**
     * Delete filtered load balancers
     *
     * @param filter load balancer filter
     * @return OperationFuture wrapper for load balancer list
     */
    public OperationFuture<List<LoadBalancer>> delete(LoadBalancerFilter filter) {
        List<LoadBalancer> loadBalancerList = findLazy(filter)
                .map(metadata -> LoadBalancer.refById(
                        metadata.getId(),
                        DataCenter.refById(metadata.getDataCenterId()))
                )
                .collect(toList());

        return delete(loadBalancerList);
    }

    /**
     * Delete load balancer list
     *
     * @param loadBalancerList load balancer list
     * @return OperationFuture wrapper for load balancer list
     */
    public OperationFuture<List<LoadBalancer>> delete(List<LoadBalancer> loadBalancerList) {
        List<JobFuture> jobs = loadBalancerList
                .stream()
                .map(reference -> delete(reference).jobFuture())
                .collect(toList());

        return new OperationFuture<>(
                loadBalancerList,
                new ParallelJobsFuture(jobs)
        );
    }
}
