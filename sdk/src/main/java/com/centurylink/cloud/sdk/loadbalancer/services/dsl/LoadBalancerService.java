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
import com.centurylink.cloud.sdk.core.services.QueryService;
import com.centurylink.cloud.sdk.loadbalancer.services.client.LoadBalancerClient;
import com.centurylink.cloud.sdk.loadbalancer.services.client.domain.LoadBalancerRequest;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerConfig;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.filter.LoadBalancerFilter;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.group.LoadBalancer;
import com.google.inject.Inject;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.function.Predicates.alwaysTrue;
import static com.centurylink.cloud.sdk.core.function.Predicates.combine;
import static com.centurylink.cloud.sdk.core.function.Predicates.in;
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

    public OperationFuture<LoadBalancer> create(LoadBalancerConfig config) {
        String dataCenterId = fetchDataCenterId(config.getDataCenter());

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

    public OperationFuture<LoadBalancer> update(LoadBalancer loadBalancer, LoadBalancerConfig config) {
        loadBalancerClient.update(
                fetchDataCenterId(loadBalancer.getDataCenter()),
                findByRef(loadBalancer).getId(),
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

    public OperationFuture<LoadBalancer> delete(LoadBalancer loadBalancer) {
        loadBalancerClient.delete(
                fetchDataCenterId(loadBalancer.getDataCenter()),
                findByRef(loadBalancer).getId()
        );

        return new OperationFuture<>(
                loadBalancer,
                new NoWaitingJobFuture()
        );
    }

    public OperationFuture<List<LoadBalancer>> delete(LoadBalancer... loadBalancer) {
        return delete(Arrays.asList(loadBalancer));
    }

    public OperationFuture<List<LoadBalancer>> delete(LoadBalancerFilter filter) {
        List<LoadBalancer> loadBalancerList = findLazy(filter)
                .map(metadata -> LoadBalancer.refById(
                        metadata.getId(),
                        DataCenter.refById(metadata.getDataCenterId()))
                )
                .collect(toList());

        return delete(loadBalancerList);
    }

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

    private String fetchDataCenterId(DataCenter dataCenter) {
        return dataCenterService.findByRef(dataCenter).getId();
    }

}
