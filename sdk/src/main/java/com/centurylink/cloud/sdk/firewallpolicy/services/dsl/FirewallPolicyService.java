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

package com.centurylink.cloud.sdk.firewallpolicy.services.dsl;

import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.base.services.dsl.DataCenterService;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.JobFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.NoWaitingJobFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.ParallelJobsFuture;
import com.centurylink.cloud.sdk.core.services.QueryService;
import com.centurylink.cloud.sdk.firewallpolicy.services.client.FirewallPolicyClient;
import com.centurylink.cloud.sdk.firewallpolicy.services.client.domain.FirewallPolicyRequest;
import com.centurylink.cloud.sdk.firewallpolicy.services.dsl.domain.FirewallPolicyConfig;
import com.centurylink.cloud.sdk.firewallpolicy.services.dsl.domain.FirewallPolicyMetadata;
import com.centurylink.cloud.sdk.firewallpolicy.services.dsl.domain.filter.FirewallPolicyFilter;
import com.centurylink.cloud.sdk.firewallpolicy.services.dsl.domain.refs.FirewallPolicy;
import com.google.inject.Inject;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.function.Predicates.alwaysTrue;
import static com.centurylink.cloud.sdk.core.function.Predicates.combine;
import static com.centurylink.cloud.sdk.core.function.Predicates.in;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

public class FirewallPolicyService implements QueryService<FirewallPolicy, FirewallPolicyFilter, FirewallPolicyMetadata> {

    private final FirewallPolicyClient firewallPolicyClient;
    private final DataCenterService dataCenterService;

    @Inject
    public FirewallPolicyService(FirewallPolicyClient loadBalancerClient, DataCenterService dataCenterService) {
        this.firewallPolicyClient = loadBalancerClient;
        this.dataCenterService = dataCenterService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<FirewallPolicyMetadata> findLazy(FirewallPolicyFilter filter) {
        checkNotNull(filter, "Filter must be not a null");

        Stream<DataCenterMetadata> dataCenters = dataCenterService.findLazy(
                filter.getDataCenterFilter()
        );

        return
            dataCenters
                .flatMap(datacenter -> firewallPolicyClient
                    .getFirewallPolicies(datacenter.getId(), filter.getDestinationAccountAlias())
                    .stream()
                )
                .filter(filter.getPredicate())
                .filter(filter.getIds().size() > 0 ?
                    combine(FirewallPolicyMetadata::getId, in(filter.getIds())) :
                    alwaysTrue()
                );
    }

    /**
     * Create firewall policy
     *
     * @param config firewall policy config
     * @return OperationFuture wrapper for firewall policy
     */
    public OperationFuture<FirewallPolicy> create(FirewallPolicyConfig config) {
        String dataCenterId = dataCenterService.findByRef(config.getDataCenter()).getId();

        FirewallPolicyMetadata firewall = firewallPolicyClient.create(
            dataCenterId,
            composeFirewallPolicyRequest(config)
        );

        return new OperationFuture<>(
            FirewallPolicy.refById(
                firewall.getId(),
                DataCenter.refById(dataCenterId)
            ),
            new NoWaitingJobFuture()
        );
    }

    /**
     * Update firewall policy
     *
     * @param firewallPolicy firewall policy
     * @param config firewall policy config
     * @return OperationFuture wrapper for firewall policy
     */
    public OperationFuture<FirewallPolicy> update(FirewallPolicy firewallPolicy, FirewallPolicyConfig config) {
        FirewallPolicyMetadata metadata = findByRef(firewallPolicy);

        firewallPolicyClient.update(
            metadata.getDataCenterId(),
            metadata.getId(),
            composeFirewallPolicyRequest(config)
        );

        return new OperationFuture<>(
            firewallPolicy,
            new NoWaitingJobFuture()
        );
    }

    /**
     * Update firewall policy list
     *
     * @param firewallPolicyList firewall policy list
     * @param config firewall policy config
     * @return OperationFuture wrapper for firewall policy list
     */
    public OperationFuture<List<FirewallPolicy>> update(
            List<FirewallPolicy> firewallPolicyList,
            FirewallPolicyConfig config
    ) {
        firewallPolicyList.forEach(firewallPolicy -> update(firewallPolicy, config));

        return new OperationFuture<>(
            firewallPolicyList,
            new NoWaitingJobFuture()
        );
    }

    /**
     * Update filtered firewall policies
     *
     * @param firewallPolicyFilter firewall policy filter
     * @param config firewall policy config
     * @return OperationFuture wrapper for firewall policy list
     */
    public OperationFuture<List<FirewallPolicy>> update(
            FirewallPolicyFilter firewallPolicyFilter,
            FirewallPolicyConfig config
    ) {
        checkNotNull(firewallPolicyFilter, "Load balancer filter must be not null");

        List<FirewallPolicy> firewallPolicyList = findLazy(firewallPolicyFilter)
            .map(metadata -> FirewallPolicy.refById(
                metadata.getId(),
                DataCenter.refById(metadata.getDataCenterId())
            ))
            .collect(toList());

        return update(firewallPolicyList, config);
    }

    /**
     * Delete firewall policy
     *
     * @param firewallPolicy firewall policy
     * @return OperationFuture wrapper for firewall policy
     */
    public OperationFuture<FirewallPolicy> delete(FirewallPolicy firewallPolicy) {
        FirewallPolicyMetadata metadata = findByRef(firewallPolicy);

        firewallPolicyClient.delete(
            metadata.getDataCenterId(),
            metadata.getId()
        );

        return new OperationFuture<>(
            firewallPolicy,
            new NoWaitingJobFuture()
        );
    }

    /**
     * Delete array of firewall policy
     *
     * @param firewallPolicies array of firewall policy
     * @return OperationFuture wrapper for firewall policy list
     */
    public OperationFuture<List<FirewallPolicy>> delete(FirewallPolicy... firewallPolicies) {
        return delete(Arrays.asList(firewallPolicies));
    }

    /**
     * Delete filtered firewall policy
     *
     * @param filter firewall policy filter
     * @return OperationFuture wrapper for firewall policy list
     */
    public OperationFuture<List<FirewallPolicy>> delete(FirewallPolicyFilter filter) {
        return delete(
            findLazy(filter)
                .map(metadata -> FirewallPolicy.refById(
                    metadata.getId(),
                    DataCenter.refById(metadata.getDataCenterId()))
                )
                .collect(toList())
        );
    }

    /**
     * Delete firewall policy list
     *
     * @param firewallPolicyList firewall policy list
     * @return OperationFuture wrapper for firewall policy list
     */
    public OperationFuture<List<FirewallPolicy>> delete(List<FirewallPolicy> firewallPolicyList) {
        List<JobFuture> jobs = firewallPolicyList
            .stream()
            .map(reference -> delete(reference).jobFuture())
            .collect(toList());

        return new OperationFuture<>(
            firewallPolicyList,
            new ParallelJobsFuture(jobs)
        );
    }

    private FirewallPolicyRequest composeFirewallPolicyRequest(FirewallPolicyConfig config) {
        return
            new FirewallPolicyRequest()
                .enabled(config.isEnabled())
                .destinationAccount(config.getDestinationAccount())
                .source(config.getSource())
                .destination(config.getDestination())
                .ports(config.getPorts());
    }
}
