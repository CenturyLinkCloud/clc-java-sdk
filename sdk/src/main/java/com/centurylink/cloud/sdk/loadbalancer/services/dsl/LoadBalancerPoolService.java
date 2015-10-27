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

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.JobFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.NoWaitingJobFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.ParallelJobsFuture;
import com.centurylink.cloud.sdk.core.services.QueryService;
import com.centurylink.cloud.sdk.loadbalancer.services.client.LoadBalancerPoolClient;
import com.centurylink.cloud.sdk.loadbalancer.services.client.domain.LoadBalancerPoolRequest;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerPoolConfig;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerPoolMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.filter.LoadBalancerPoolFilter;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.group.LoadBalancer;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.pool.LoadBalancerPool;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.function.Predicates.alwaysTrue;
import static com.centurylink.cloud.sdk.core.function.Predicates.combine;
import static com.centurylink.cloud.sdk.core.function.Predicates.in;
import static com.centurylink.cloud.sdk.core.preconditions.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

public class LoadBalancerPoolService implements QueryService<LoadBalancerPool, LoadBalancerPoolFilter, LoadBalancerPoolMetadata> {

    private final LoadBalancerPoolClient loadBalancerPoolClient;
    private final LoadBalancerService loadBalancerService;

    private final Supplier<LoadBalancerNodeService> loadBalancerNodeServiceSupplier;


    public interface LoadBalancerNodeServiceSupplier extends Supplier<LoadBalancerNodeService> {}

    public LoadBalancerPoolService(
            LoadBalancerPoolClient loadBalancerPoolClient,
            LoadBalancerService loadBalancerService,
            LoadBalancerNodeServiceSupplier loadBalancerNodeServiceSupplier
    ) {
        this.loadBalancerPoolClient = loadBalancerPoolClient;
        this.loadBalancerService = loadBalancerService;
        this.loadBalancerNodeServiceSupplier = loadBalancerNodeServiceSupplier;
    }

    private LoadBalancerNodeService loadBalancerNodeService() {
        return loadBalancerNodeServiceSupplier.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<LoadBalancerPoolMetadata> findLazy(LoadBalancerPoolFilter filter) {
        checkNotNull(filter, "Filter must be not a null");

        Stream<LoadBalancerMetadata> loadBalancers = loadBalancerService.findLazy(
                filter.getLoadBalancerFilter()
        );

        return
            loadBalancers
                .flatMap(loadBalancerMetadata -> loadBalancerPoolClient
                    .getLoadBalancerPools(loadBalancerMetadata.getDataCenterId(), loadBalancerMetadata.getId())
                    .stream()
                )
                .filter(filter.getPredicate())
                .filter(
                    filter.getIds().size() > 0 ?
                        combine(LoadBalancerPoolMetadata::getId, in(filter.getIds())) :
                        alwaysTrue()
                );
    }

    /**
     * Create load balancer pool
     *
     * @param config load balancer pool config
     * @return OperationFuture wrapper for load balancer pool
     */
    public OperationFuture<LoadBalancerPool> create(LoadBalancerPoolConfig config) {
        LoadBalancer loadBalancer = config.getLoadBalancer();
        LoadBalancerMetadata loadBalancerMetadata = loadBalancerService.findByRef(loadBalancer);

        LoadBalancerPoolMetadata metadata = loadBalancerPoolClient.create(
            loadBalancerMetadata.getDataCenterId(),
            loadBalancerMetadata.getId(),
            new LoadBalancerPoolRequest()
                .port(config.getPort())
                .method(config.getMethod())
                .persistence(config.getPersistence())
        );

        LoadBalancerPool pool = LoadBalancerPool.refById(metadata.getId(), loadBalancer);

        return new OperationFuture<>(
            pool,
            addLoadBalancerNodes(config, pool)
        );
    }

    private JobFuture addLoadBalancerNodes(LoadBalancerPoolConfig poolConfig, LoadBalancerPool loadBalancerPool) {
        if (poolConfig.getNodes() != null) {
            return loadBalancerNodeService()
                .update(loadBalancerPool, poolConfig.getNodes())
                .jobFuture();
        }
        return new NoWaitingJobFuture();
    }

    /**
     * Update load balancer pool
     *
     * @param loadBalancerPool load balancer pool
     * @param config load balancer pool config
     * @return OperationFuture wrapper for load balancer pool
     */
    public OperationFuture<LoadBalancerPool> update(LoadBalancerPool loadBalancerPool, LoadBalancerPoolConfig config) {
        LoadBalancerPoolMetadata loadBalancerPoolMetadata = findByRef(loadBalancerPool);

        loadBalancerPoolClient.update(
            loadBalancerPoolMetadata.getDataCenterId(),
            loadBalancerPoolMetadata.getLoadBalancerId(),
            loadBalancerPoolMetadata.getId(),
            new LoadBalancerPoolRequest()
                .method(config.getMethod())
                .persistence(config.getPersistence())
        );

        return new OperationFuture<>(
            loadBalancerPool,
            addLoadBalancerNodes(config, loadBalancerPool)
        );
    }

    /**
     * Update load balancer pools for group
     *
     * @param loadBalancer load balancer pool
     * @param configs load balancer pool configs
     * @return OperationFuture wrapper for load balancer pool
     */
    public OperationFuture<LoadBalancer> update(
        LoadBalancer loadBalancer, List<LoadBalancerPoolConfig> configs) {

        List<LoadBalancerPoolMetadata> poolsForGroup = find(
            new LoadBalancerPoolFilter().loadBalancers(loadBalancer)
        );

        return new OperationFuture<>(
            loadBalancer,
            new ParallelJobsFuture(
                configs.stream()
                    .map(poolConfig -> {
                        return createOrUpdate(poolConfig, loadBalancer, poolsForGroup);
                    })
                    .map(OperationFuture::jobFuture)
                    .collect(toList())
            )
        );
    }

    protected OperationFuture createOrUpdate(LoadBalancerPoolConfig poolConfig, LoadBalancer loadBalancer,
        List<LoadBalancerPoolMetadata> poolsForGroup) {
        //try to update
        if (poolConfig.getId() != null) {
            return update(
                LoadBalancerPool.refById(poolConfig.getId(), loadBalancer),
                poolConfig
            );
        } else {
            //try to update by port
            LoadBalancerPoolMetadata poolToUpdate = poolsForGroup.stream()
                .filter(pool -> pool.getPort().equals(poolConfig.getPort()))
                .findFirst()
                .orElse(null);

            if (poolToUpdate != null) {
                return update(
                    LoadBalancerPool.refById(poolToUpdate.getId(), loadBalancer),
                    poolConfig
                );
            }
        }
        return create(poolConfig.loadBalancer(loadBalancer));
    }
    /**
     * Update load balancer pool list
     *
     * @param loadBalancerPoolList load balancer pool list
     * @param config load balancer pool config
     * @return OperationFuture wrapper for load balancer pool list
     */
    public OperationFuture<List<LoadBalancerPool>> update(
            List<LoadBalancerPool> loadBalancerPoolList,
            LoadBalancerPoolConfig config
    ) {
        loadBalancerPoolList.forEach(pool -> update(pool, config));

        return new OperationFuture<>(
            loadBalancerPoolList,
            new NoWaitingJobFuture()
        );
    }

    /**
     * Update filtered balancer pools
     *
     * @param loadBalancerPoolFilter load balancer pool filter
     * @param config load balancer pool config
     * @return OperationFuture wrapper for load balancer pool list
     */
    public OperationFuture<List<LoadBalancerPool>> update(
            LoadBalancerPoolFilter loadBalancerPoolFilter,
            LoadBalancerPoolConfig config
    ) {
        checkNotNull(loadBalancerPoolFilter, "Load balancer pool filter must be not null");

        List<LoadBalancerPool> loadBalancerPoolList = findLazy(loadBalancerPoolFilter)
            .map(metadata -> LoadBalancerPool.refById(
                metadata.getId(),
                LoadBalancer.refById(
                    metadata.getLoadBalancerId(),
                    DataCenter.refById(metadata.getDataCenterId())
                )
            ))
            .collect(toList());

        return update(loadBalancerPoolList, config);
    }

    /**
     * Delete load balancer pool
     *
     * @param loadBalancerPool load balancer pool
     * @return OperationFuture wrapper for load balancer pool
     */
    public OperationFuture<LoadBalancerPool> delete(LoadBalancerPool loadBalancerPool) {
        LoadBalancerPoolMetadata loadBalancerPoolMetadata = findByRef(loadBalancerPool);

        loadBalancerPoolClient.delete(
            loadBalancerPoolMetadata.getDataCenterId(),
            loadBalancerPoolMetadata.getLoadBalancerId(),
            loadBalancerPoolMetadata.getId()
        );

        return new OperationFuture<>(
            loadBalancerPool,
            new NoWaitingJobFuture()
        );
    }

    /**
     * Delete array of load balancer pool
     *
     * @param loadBalancerPool array of load balancer pool
     * @return OperationFuture wrapper for load balancer pool list
     */
    public OperationFuture<List<LoadBalancerPool>> delete(LoadBalancerPool... loadBalancerPool) {
        return delete(Arrays.asList(loadBalancerPool));
    }

    /**
     * Delete filtered load balancer pools
     * @param filter load balancer pool filter
     * @return OperationFuture wrapper for load balancer pool list
     */
    public OperationFuture<List<LoadBalancerPool>> delete(LoadBalancerPoolFilter filter) {
        List<LoadBalancerPool> loadBalancerPoolList = findLazy(filter)
            .map(metadata -> LoadBalancerPool.refById(
                metadata.getId(),
                LoadBalancer.refById(
                    metadata.getLoadBalancerId(),
                    DataCenter.refById(metadata.getDataCenterId())
                )
            ))
            .collect(toList());

        return delete(loadBalancerPoolList);
    }

    /**
     * Delete load balancer pool list
     *
     * @param loadBalancerPoolList load balancer pool list
     * @return OperationFuture wrapper for load balancer pool list
     */
    public OperationFuture<List<LoadBalancerPool>> delete(List<LoadBalancerPool> loadBalancerPoolList) {
        List<JobFuture> jobs = loadBalancerPoolList
            .stream()
            .map(reference -> delete(reference).jobFuture())
            .collect(toList());

        return new OperationFuture<>(
            loadBalancerPoolList,
            new ParallelJobsFuture(jobs)
        );
    }

}
