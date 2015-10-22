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

package com.centurylink.cloud.sdk.policy.services.dsl;

import com.centurylink.cloud.sdk.policy.services.client.AutoscalePolicyClient;
import com.centurylink.cloud.sdk.policy.services.client.domain.autoscale.SetAutoscalePolicyRequest;
import com.centurylink.cloud.sdk.policy.services.client.domain.autoscale.AutoscalePolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.autoscale.filter.AutoscalePolicyFilter;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.autoscale.refs.AutoscalePolicy;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.JobFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.NoWaitingJobFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.ParallelJobsFuture;
import com.centurylink.cloud.sdk.core.services.QueryService;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.function.Predicates.alwaysTrue;
import static com.centurylink.cloud.sdk.core.function.Predicates.combine;
import static com.centurylink.cloud.sdk.core.function.Predicates.in;
import static com.centurylink.cloud.sdk.core.preconditions.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

public class AutoscalePolicyService implements QueryService<AutoscalePolicy, AutoscalePolicyFilter, AutoscalePolicyMetadata> {

    private final AutoscalePolicyClient autoscalePolicyClient;
    private final ServerService serverService;

    public AutoscalePolicyService(AutoscalePolicyClient autoscalePolicyClient, ServerService serverService) {
        this.autoscalePolicyClient = autoscalePolicyClient;
        this.serverService = serverService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<AutoscalePolicyMetadata> findLazy(AutoscalePolicyFilter filter) {
        checkNotNull(filter, "Filter must be not a null");

        return autoscalePolicyClient
            .getAutoscalePolicies()
            .stream()
            .filter(filter.getPredicate())
            .filter(
                filter.getIds().size() > 0 ?
                    combine(AutoscalePolicyMetadata::getId, in(filter.getIds())) :
                    alwaysTrue()
            );
    }

    /**
     * Set autoscale policy on server
     *
     * @param autoscalePolicy autoscale policy
     * @param server server
     * @return OperationFuture wrapper for autoscalePolicy
     */
    public OperationFuture<Server> setAutoscalePolicyOnServer(AutoscalePolicy autoscalePolicy, Server server) {
        autoscalePolicyClient.setAutoscalePolicyOnServer(
            serverService
                .findByRef(server)
                .getId(),
            new SetAutoscalePolicyRequest()
                .id(
                    findByRef(autoscalePolicy).getId()
                )
        );

        return new OperationFuture<>(server, new NoWaitingJobFuture());
    }

    /**
     * set autoscale policy on servers
     *
     * @param autoscalePolicy autoscale policy
     * @param serverList server list
     * @return OperationFuture wrapper for server list
     */
    public OperationFuture<List<Server>> setAutoscalePolicyOnServer(
            AutoscalePolicy autoscalePolicy,
            List<Server> serverList
    ) {
        List<JobFuture> jobs = serverList
            .stream()
            .map(server -> setAutoscalePolicyOnServer(autoscalePolicy, server).jobFuture())
            .collect(toList());

        return
            new OperationFuture<>(
                serverList,
                new ParallelJobsFuture(jobs)
            );
    }

    /**
     * set autoscale policy on servers
     *
     * @param autoscalePolicy autoscale policy
     * @param servers servers
     * @return OperationFuture wrapper for servers
     */
    public OperationFuture<List<Server>> setAutoscalePolicyOnServer(
            AutoscalePolicy autoscalePolicy,
            Server... servers
    ) {
        return setAutoscalePolicyOnServer(autoscalePolicy, Arrays.asList(servers));
    }

    /**
     * set autoscale policy on filtered servers
     *
     * @param autoscalePolicy autoscale policy
     * @param serverFilter server filter
     * @return OperationFuture wrapper for servers
     */
    public OperationFuture<List<Server>> setAutoscalePolicyOnServer(
            AutoscalePolicy autoscalePolicy,
            ServerFilter serverFilter
    ) {
        return
            setAutoscalePolicyOnServer(
                autoscalePolicy,
                serverService
                    .findLazy(serverFilter)
                    .map(serverMetadata -> Server.refById(serverMetadata.getId()))
                    .collect(toList())
            );
    }

    /**
     * get autoscale policy on server
     *
     * @param server server
     * @return AutoscalePolicyMetadata
     */
    public AutoscalePolicyMetadata getAutoscalePolicyOnServer(Server server) {
        return
            autoscalePolicyClient
                .getAutoscalePolicyOnServer(
                    serverService
                        .findByRef(server)
                        .getId()
                );
    }

    /**
     * remove autoscale policy on server
     *
     * @param server server
     * @return OperationFuture wrapper for server
     */
    public OperationFuture<Server> removeAutoscalePolicyOnServer(Server server) {
        autoscalePolicyClient
            .removeAutoscalePolicyOnServer(
                serverService.findByRef(server).getId()
            );

        return new OperationFuture<>(server, new NoWaitingJobFuture());
    }

    /**
     * remove autoscale policy on servers
     *
     * @param serverList server list
     * @return OperationFuture wrapper for server list
     */
    public OperationFuture<List<Server>> removeAutoscalePolicyOnServer(List<Server> serverList) {
        List<JobFuture> jobs = serverList
            .stream()
            .map(server -> removeAutoscalePolicyOnServer(server).jobFuture())
            .collect(toList());

        return
            new OperationFuture<>(
                serverList,
                new ParallelJobsFuture(jobs)
            );
    }

    /**
     * remove autoscale policy on servers
     *
     * @param servers servers
     * @return OperationFuture wrapper for servers
     */
    public OperationFuture<List<Server>> removeAutoscalePolicyOnServer(Server... servers) {
        return removeAutoscalePolicyOnServer(Arrays.asList(servers));
    }

    /**
     * remove autoscale policy on filtered servers
     *
     * @param serverFilter server filter
     * @return OperationFuture wrapper for servers
     */
    public OperationFuture<List<Server>> removeAutoscalePolicyOnServer(ServerFilter serverFilter) {
        return
            removeAutoscalePolicyOnServer(
                serverService
                    .findLazy(serverFilter)
                    .map(serverMetadata -> Server.refById(serverMetadata.getId()))
                    .collect(toList())
            );
    }
}
