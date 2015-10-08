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

import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.base.services.dsl.DataCenterService;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.JobFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.NoWaitingJobFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.ParallelJobsFuture;
import com.centurylink.cloud.sdk.core.services.QueryService;
import com.centurylink.cloud.sdk.policy.services.client.PolicyClient;
import com.centurylink.cloud.sdk.policy.services.client.domain.AntiAffinityPolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.client.domain.AntiAffinityPolicyRequest;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AntiAffinityPolicyConfig;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.filters.AntiAffinityPolicyFilter;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.refs.AntiAffinityPolicy;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.function.Predicates.alwaysTrue;
import static com.centurylink.cloud.sdk.core.function.Predicates.combine;
import static com.centurylink.cloud.sdk.core.function.Predicates.in;
import static com.centurylink.cloud.sdk.core.function.Predicates.isAlwaysTruePredicate;
import static com.centurylink.cloud.sdk.core.function.Predicates.notNull;
import static com.centurylink.cloud.sdk.core.services.filter.Filters.nullable;
import static com.centurylink.cloud.sdk.core.preconditions.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Service provide operations for query and manipulate anti-affinity policies
 *
 * @author Aliaksandr Krasitski
 */
public class AntiAffinityService implements
    QueryService<AntiAffinityPolicy, AntiAffinityPolicyFilter, AntiAffinityPolicyMetadata> {

    private PolicyClient client;
    private DataCenterService dataCenterService;

    public AntiAffinityService(PolicyClient client, DataCenterService dataCenterService) {
        this.client = client;
        this.dataCenterService = dataCenterService;
    }

    /**
     * Create Anti-affinity policy
     *
     * @param createConfig policy config
     * @return OperationFuture wrapper for AntiAffinityPolicy
     */
    public OperationFuture<AntiAffinityPolicy> create(AntiAffinityPolicyConfig createConfig) {
        AntiAffinityPolicyMetadata policy = client.createAntiAffinityPolicy(
            new AntiAffinityPolicyRequest()
                .name(createConfig.getName())
                .location(dataCenterService.findByRef(createConfig.getDataCenter()).getId())
        );

        return new OperationFuture<>(
            AntiAffinityPolicy.refById(policy.getId()),
            new NoWaitingJobFuture()
        );
    }

    /**
     * Update Anti-affinity policy
     *
     * @param policyRef policy reference
     * @param modifyConfig update policy config
     * @return OperationFuture wrapper for AntiAffinityPolicy
     */
    public OperationFuture<AntiAffinityPolicy> modify(AntiAffinityPolicy policyRef,
        AntiAffinityPolicyConfig modifyConfig) {

        client.modifyAntiAffinityPolicy(
            findByRef(policyRef).getId(),
            new AntiAffinityPolicyRequest()
                .name(modifyConfig.getName())
        );

        return new OperationFuture<>(
            policyRef,
            new NoWaitingJobFuture()
        );
    }

    /**
     * Update Anti-affinity policies
     *
     * @param policyRefs policy references
     * @param modifyConfig update policy config
     * @return OperationFuture wrapper for list of AntiAffinityPolicy
     */
    public OperationFuture<List<AntiAffinityPolicy>> modify(List<AntiAffinityPolicy> policyRefs,
        AntiAffinityPolicyConfig modifyConfig) {

        policyRefs.forEach(ref -> modify(ref, modifyConfig));

        return new OperationFuture<>(
            policyRefs,
            new NoWaitingJobFuture()
        );
    }

    /**
     * Update Anti-affinity policies
     *
     * @param filter the search policies criteria
     * @param modifyConfig update policy config
     * @return OperationFuture wrapper for list of AntiAffinityPolicy
     */
    public OperationFuture<List<AntiAffinityPolicy>> modify(AntiAffinityPolicyFilter filter,
        AntiAffinityPolicyConfig modifyConfig) {

        return modify(getRefsFromFilter(filter), modifyConfig);
    }

    /**
     * Remove Anti-affinity policy
     *
     * @param policyRef policy reference
     * @return OperationFuture wrapper for AntiAffinityPolicy
     */
    public OperationFuture<AntiAffinityPolicy> delete(AntiAffinityPolicy policyRef) {
        client.deleteAntiAffinityPolicy(findByRef(policyRef).getId());

        return new OperationFuture<>(
            policyRef,
            new NoWaitingJobFuture()
        );
    }

    /**
     * Remove Anti-affinity policies
     *
     * @param policyRefs policy references
     * @return OperationFuture wrapper for list of AntiAffinityPolicy
     */
    public OperationFuture<List<AntiAffinityPolicy>> delete(AntiAffinityPolicy... policyRefs) {
        List<AntiAffinityPolicy> policiesList = Arrays.asList(policyRefs);

        List<JobFuture> jobs = policiesList.stream()
            .map(ref -> delete(ref).jobFuture())
            .collect(toList());

        return new OperationFuture<>(
            policiesList,
            new ParallelJobsFuture(jobs)
        );
    }

    /**
     * Remove Anti-affinity policies
     *
     * @param filter the search policies criteria
     * @return OperationFuture wrapper for list of AntiAffinityPolicy
     */
    public OperationFuture<List<AntiAffinityPolicy>> delete(AntiAffinityPolicyFilter filter) {
        List<AntiAffinityPolicy> policyRefs = getRefsFromFilter(filter);
        return delete(policyRefs.toArray(new AntiAffinityPolicy[policyRefs.size()]));
    }


    private List<AntiAffinityPolicy> getRefsFromFilter(AntiAffinityPolicyFilter filter) {
        return findLazy(filter)
            .map(metadata -> AntiAffinityPolicy.refById(metadata.getId()))
            .collect(toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<AntiAffinityPolicyMetadata> findLazy(AntiAffinityPolicyFilter filter) {
        checkNotNull(filter, "Criteria must be not null");

        return
            filter.applyFindLazy(criteria -> {
                if (isTruePredicate(criteria) && criteria.getIds().size() > 0) {
                    return
                        criteria.getIds().stream()
                            .map(nullable(curId -> client.getAntiAffinityPolicy(curId)))
                            .filter(notNull());
                }

                List<String> dataCenterIds =
                    dataCenterService
                        .findLazy(criteria.getDataCenterFilter())
                        .map(DataCenterMetadata::getId)
                        .map(String::toUpperCase)
                        .collect(toList());

                return
                    client.getAntiAffinityPolicies()
                        .stream()
                        .filter(criteria.getPredicate())
                        .filter((dataCenterIds.size() > 0) ?
                            combine(AntiAffinityPolicyMetadata::getLocation, in(dataCenterIds)) : alwaysTrue())
                        .filter((criteria.getIds().size() > 0) ?
                            combine(AntiAffinityPolicyMetadata::getId, in(criteria.getIds())) : alwaysTrue()
                        );

            });
    }

    private boolean isTruePredicate(AntiAffinityPolicyFilter criteria) {
        return isAlwaysTruePredicate(criteria.getPredicate()) &&
            isAlwaysTruePredicate(criteria.getDataCenterFilter().getPredicate());
    }
}
