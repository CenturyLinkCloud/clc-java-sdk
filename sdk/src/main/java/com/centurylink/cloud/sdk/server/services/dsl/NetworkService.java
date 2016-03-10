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

import com.centurylink.cloud.sdk.base.services.client.ExperimentalQueueClient;
import com.centurylink.cloud.sdk.base.services.client.QueueClient;
import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.base.services.dsl.DataCenterService;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.JobFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.NoWaitingJobFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.ParallelJobsFuture;
import com.centurylink.cloud.sdk.core.client.domain.NetworkLink;
import com.centurylink.cloud.sdk.core.services.QueryService;
import com.centurylink.cloud.sdk.server.services.client.NetworkClient;
import com.centurylink.cloud.sdk.server.services.client.domain.network.NetworkMetadata;
import com.centurylink.cloud.sdk.server.services.client.domain.network.UpdateNetworkRequest;
import com.centurylink.cloud.sdk.server.services.dsl.domain.network.UpdateNetworkConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.network.filters.NetworkFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.network.refs.Network;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toList;

import static com.centurylink.cloud.sdk.core.function.Predicates.alwaysTrue;
import static com.centurylink.cloud.sdk.core.function.Predicates.combine;
import static com.centurylink.cloud.sdk.core.function.Predicates.in;

public class NetworkService implements QueryService<Network, NetworkFilter, NetworkMetadata> {

    private final NetworkClient networkClient;
    private final DataCenterService dataCenterService;
    private final ExperimentalQueueClient queueClient;

    public NetworkService(NetworkClient networkClient, DataCenterService dataCenterService,
                          ExperimentalQueueClient queueClient) {
        this.networkClient = networkClient;
        this.dataCenterService = dataCenterService;
        this.queueClient = queueClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<NetworkMetadata> findLazy(NetworkFilter filter) {
        checkFilter(filter);

        return
            dataCenterService
                .findLazy(
                    filter.getDataCenterFilter()
                )
                .flatMap(
                    datacenter -> networkClient
                        .getNetworks(datacenter.getId())
                        .stream()
                )
                .filter(filter.getPredicate())
                .filter(
                    filter.getIds().size() > 0 ?
                        combine(NetworkMetadata::getId, in(filter.getIds())) :
                        alwaysTrue()
                );
    }

    /**
     * Release the network for datacenter
     *
     * @param dataCenter datacenter reference
     * @return OperationFuture wrapper for network
     */
    public OperationFuture<Network> release(Network network, DataCenter dataCenter) {
        DataCenterMetadata dataCenterMetadata = dataCenterService.findByRef(dataCenter);
        NetworkMetadata networkMetadata = findByRef(network);

        networkClient.release(
            networkMetadata.getId(),
            dataCenterMetadata.getId()
        );

        return new OperationFuture<>(network, new NoWaitingJobFuture());
    }

    /**
     * Update the network
     *
     * @param network network
     * @param config update config
     * @return OperationFuture wrapper for network
     */
    public OperationFuture<Network> update(Network network, UpdateNetworkConfig config) {
        NetworkMetadata networkMetadata = findByRef(network);

        networkClient.update(
            networkMetadata.getDataCenterId(),
            networkMetadata.getId(),
            new UpdateNetworkRequest()
                .name(config.getName())
                .description(config.getDescription())
        );

        return new OperationFuture<>(network, new NoWaitingJobFuture());
    }

    /**
     * Update the network
     *
     * @param networkList network list
     * @param config update config
     * @return OperationFuture wrapper for network list
     */
    public OperationFuture<List<Network>> update(List<Network> networkList, UpdateNetworkConfig config) {
        networkList.forEach(network -> update(network, config));

        return new OperationFuture<>(networkList, new NoWaitingJobFuture());
    }

    /**
     * Update the network
     *
     * @param filter network filter
     * @param config update config
     * @return OperationFuture wrapper for network list
     */
    public OperationFuture<List<Network>> update(NetworkFilter filter, UpdateNetworkConfig config) {
        checkFilter(filter);

        List<Network> networkList = findLazy(filter)
            .map(metadata -> Network.refById(metadata.getId()))
            .collect(toList());

        return update(networkList, config);
    }

    /**
     * Claim a network for datacenter
     *
     * @param dataCenter datacenter reference
     * @return OperationFuture wrapper for dataCenter
     */
    public OperationFuture<DataCenter> claim(DataCenter dataCenter) {
        NetworkLink response = networkClient.claim(
            dataCenterService.findByRef(dataCenter).getId()
        );

        return
            new OperationFuture<>(
                dataCenter,
                response.getOperationId(),
                queueClient
            );
    }

    /**
     * Claim a networks for datacenters
     *
     * @param dataCenters datacenter reference list
     * @return OperationFuture wrapper for dataCenter list
     */
    public OperationFuture<List<DataCenter>> claim(List<DataCenter> dataCenters) {
        List<JobFuture> jobs = dataCenters
            .stream()
            .map(dataCenter -> claim(dataCenter).jobFuture())
            .collect(toList());

        return
            new OperationFuture<>(
                dataCenters,
                new ParallelJobsFuture(jobs)
            );
    }

    /**
     * Claim a networks for datacenters
     *
     * @param dataCenters datacenter references
     * @return OperationFuture wrapper for dataCenter list
     */
    public OperationFuture<List<DataCenter>> claim(DataCenter... dataCenters) {
        return claim(Arrays.asList(dataCenters));
    }



}
