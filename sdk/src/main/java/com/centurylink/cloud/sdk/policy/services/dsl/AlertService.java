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

import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.JobFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.NoWaitingJobFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.ParallelJobsFuture;
import com.centurylink.cloud.sdk.core.services.QueryService;
import com.centurylink.cloud.sdk.policy.services.client.PolicyClient;
import com.centurylink.cloud.sdk.policy.services.client.domain.AlertPolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AlertPolicyConfig;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.PolicyConverter;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.filters.AlertPolicyFilter;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.refs.AlertPolicy;

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
 * Service provide operations for query and manipulate alert policies
 *
 * @author Aliaksandr Krasitski
 */
public class AlertService implements
    QueryService<AlertPolicy, AlertPolicyFilter, AlertPolicyMetadata> {

    private PolicyClient client;
    private PolicyConverter converter;

    public AlertService(PolicyClient client, PolicyConverter converter) {
        this.client = client;
        this.converter = converter;
    }

    /**
     * Create Alert policy
     *
     * @param createConfig policy config
     * @return OperationFuture wrapper for AlertPolicy
     */
    public OperationFuture<AlertPolicy> create(AlertPolicyConfig createConfig) {
        AlertPolicyMetadata policy = client.createAlertPolicy(converter.buildCreateAlertPolicyRequest(createConfig));

        return new OperationFuture<>(
            AlertPolicy.refById(policy.getId()),
            new NoWaitingJobFuture()
        );
    }

    /**
     * Update Alert policy
     *
     * @param policyRef policy reference
     * @param modifyConfig update policy config
     * @return OperationFuture wrapper for AlertPolicy
     */
    public OperationFuture<AlertPolicy> modify(AlertPolicy policyRef,
        AlertPolicyConfig modifyConfig) {

        AlertPolicyMetadata policyToUpdate = findByRef(policyRef);


        client.modifyAlertPolicy(
            policyToUpdate.getId(),
            converter.buildModifyAlertPolicyRequest(modifyConfig, policyToUpdate)
        );

        return new OperationFuture<>(
            policyRef,
            new NoWaitingJobFuture()
        );
    }

    /**
     * Update Alert policies
     *
     * @param policyRefs policy references
     * @param modifyConfig update policy config
     * @return OperationFuture wrapper for list of AlertPolicy
     */
    public OperationFuture<List<AlertPolicy>> modify(List<AlertPolicy> policyRefs,
        AlertPolicyConfig modifyConfig) {

        policyRefs.forEach(ref -> modify(ref, modifyConfig));

        return new OperationFuture<>(
            policyRefs,
            new NoWaitingJobFuture()
        );
    }

    /**
     * Update Alert policies
     *
     * @param filter the search policies criteria
     * @param modifyConfig update policy config
     * @return OperationFuture wrapper for list of AlertPolicy
     */
    public OperationFuture<List<AlertPolicy>> modify(AlertPolicyFilter filter,
        AlertPolicyConfig modifyConfig) {

        return modify(getRefsFromFilter(filter), modifyConfig);
    }

    /**
     * Remove Alert policy
     *
     * @param policyRef policy reference
     * @return OperationFuture wrapper for AlertPolicy
     */
    public OperationFuture<AlertPolicy> delete(AlertPolicy policyRef) {
        client.deleteAlertPolicy(findByRef(policyRef).getId());

        return new OperationFuture<>(
            policyRef,
            new NoWaitingJobFuture()
        );
    }

    /**
     * Remove Alert policies
     *
     * @param policyRefs policy references
     * @return OperationFuture wrapper for list of AlertPolicy
     */
    public OperationFuture<List<AlertPolicy>> delete(AlertPolicy... policyRefs) {
        List<AlertPolicy> policiesList = Arrays.asList(policyRefs);

        List<JobFuture> jobs = policiesList.stream()
            .map(ref -> delete(ref).jobFuture())
            .collect(toList());

        return new OperationFuture<>(
            policiesList,
            new ParallelJobsFuture(jobs)
        );
    }

    /**
     * Remove Alert policies
     *
     * @param filter the search policies criteria
     * @return OperationFuture wrapper for list of AlertPolicy
     */
    public OperationFuture<List<AlertPolicy>> delete(AlertPolicyFilter filter) {
        List<AlertPolicy> policyRefs = getRefsFromFilter(filter);
        return delete(policyRefs.toArray(new AlertPolicy[policyRefs.size()]));
    }


    private List<AlertPolicy> getRefsFromFilter(AlertPolicyFilter filter) {
        return findLazy(filter)
            .map(metadata -> AlertPolicy.refById(metadata.getId()))
            .collect(toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<AlertPolicyMetadata> findLazy(AlertPolicyFilter filter) {
        checkNotNull(filter, "Criteria must be not null");

        return
            filter.applyFindLazy(criteria -> {
                if (isAlwaysTruePredicate(criteria.getPredicate()) &&
                    criteria.getIds().size() > 0) {
                    return
                        criteria.getIds().stream()
                            .map(nullable(curId -> client.getAlertPolicy(curId)))
                            .filter(notNull());
                } else {
                    return
                        client.getAlertPolicies()
                            .stream()
                            .filter(criteria.getPredicate())
                            .filter((criteria.getIds().size() > 0) ?
                                    combine(AlertPolicyMetadata::getId, in(criteria.getIds())) : alwaysTrue()
                            );
                }
            });
    }
}
