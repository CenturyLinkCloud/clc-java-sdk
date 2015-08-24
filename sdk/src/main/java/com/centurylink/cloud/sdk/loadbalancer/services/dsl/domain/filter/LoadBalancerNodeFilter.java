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

package com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.filter;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.core.services.filter.AbstractResourceFilter;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.AndEvaluation;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.OrEvaluation;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.SingleFilterEvaluation;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerNodeMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerPoolMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.pool.LoadBalancerPool;
import java.util.function.Predicate;

import static com.centurylink.cloud.sdk.core.function.Predicates.alwaysTrue;

public class LoadBalancerNodeFilter extends AbstractResourceFilter<LoadBalancerNodeFilter> {

    private Predicate<LoadBalancerNodeMetadata> predicate = alwaysTrue();

    private LoadBalancerPoolFilter loadBalancerPoolFilter = new LoadBalancerPoolFilter(
            new LoadBalancerFilter(
                new DataCenterFilter(alwaysTrue()),
                alwaysTrue()
            ),
            alwaysTrue()
    );

    public LoadBalancerNodeFilter() {}

    public LoadBalancerNodeFilter(
            LoadBalancerPoolFilter loadBalancerPoolFilter, Predicate<LoadBalancerNodeMetadata> predicate
    ) {
        this.loadBalancerPoolFilter = loadBalancerPoolFilter;
        this.predicate = predicate;
    }

    /**
     * Method allow to provide custom filter predicate for load balancer pools
     *
     * @param predicate is not null filtering expression
     * @return {@link LoadBalancerNodeFilter}
     */
    public LoadBalancerNodeFilter loadBalancerPoolsWhere(Predicate<LoadBalancerPoolMetadata> predicate) {
        loadBalancerPoolFilter.where(predicate);
        return this;
    }

    /**
     * Method allow to filter by load balancer pool IDs. Filtering is strong case sensitive.
     *
     * @param ids the array of load balancer pool id
     * @return {@link LoadBalancerNodeFilter}
     */
    public LoadBalancerNodeFilter loadBalancerPools(String... ids) {
        loadBalancerPoolFilter.id(ids);
        return this;
    }

    /**
     * Method allow to filter by load balancer pool references.
     *
     * @param loadBalancerPools is list of LoadBalancerPool references
     * @return {@link LoadBalancerNodeFilter}
     */
    public LoadBalancerNodeFilter loadBalancerPools(LoadBalancerPool... loadBalancerPools) {
        loadBalancerPoolFilter.loadBalancerPools(loadBalancerPools);
        return this;
    }

    /**
     * Method allow to provide custom filter predicate
     *
     * @param predicate is not null filtering expression
     * @return {@link LoadBalancerNodeFilter}
     */
    public LoadBalancerNodeFilter where(Predicate<LoadBalancerNodeMetadata> predicate) {
        this.predicate = this.predicate.and(predicate);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoadBalancerNodeFilter and(LoadBalancerNodeFilter otherFilter) {
        if (evaluation instanceof SingleFilterEvaluation &&
            otherFilter.evaluation instanceof SingleFilterEvaluation) {
            return
                new LoadBalancerNodeFilter(
                    getLoadBalancerPoolFilter().and(otherFilter.getLoadBalancerPoolFilter()),
                    getPredicate().and(otherFilter.getPredicate())
                );
        } else {
            evaluation = new AndEvaluation<>(evaluation, otherFilter, LoadBalancerNodeMetadata::getIpAddress);

            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoadBalancerNodeFilter or(LoadBalancerNodeFilter otherFilter) {
        evaluation = new OrEvaluation<>(evaluation, otherFilter, LoadBalancerNodeMetadata::getIpAddress);
        return this;
    }

    public LoadBalancerPoolFilter getLoadBalancerPoolFilter() {
        return loadBalancerPoolFilter;
    }

    public Predicate<LoadBalancerNodeMetadata> getPredicate() {
        return predicate;
    }
}
