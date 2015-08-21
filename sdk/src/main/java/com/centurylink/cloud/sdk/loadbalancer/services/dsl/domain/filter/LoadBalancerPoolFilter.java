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
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerPoolMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.group.LoadBalancer;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.pool.LoadBalancerPool;
import com.centurylink.cloud.sdk.core.services.filter.Filter;
import com.centurylink.cloud.sdk.core.function.Streams;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.centurylink.cloud.sdk.core.function.Predicates.alwaysTrue;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;

public class LoadBalancerPoolFilter extends AbstractResourceFilter<LoadBalancerPoolFilter> {

    private List<String> ids = new ArrayList<>();
    private Predicate<LoadBalancerPoolMetadata> predicate = alwaysTrue();

    private LoadBalancerFilter loadBalancerFilter = new LoadBalancerFilter(
            new DataCenterFilter(alwaysTrue()),
            alwaysTrue()
    );

    public LoadBalancerPoolFilter() {
    }

    public LoadBalancerPoolFilter(
            LoadBalancerFilter loadBalancerFilter, Predicate<LoadBalancerPoolMetadata> predicate
    ) {
        this.loadBalancerFilter = loadBalancerFilter;
        this.predicate = predicate;
    }

    public LoadBalancerPoolFilter loadBalancersWhere(Predicate<LoadBalancerMetadata> predicate) {
        checkNotNull(predicate, "Predicate must be not a null");
        loadBalancerFilter.where(predicate);
        return this;
    }

    public LoadBalancerPoolFilter loadBalancers(String... ids) {
        loadBalancerFilter.id(ids);
        return this;
    }

    public LoadBalancerPoolFilter loadBalancers(LoadBalancer... loadBalancers) {
        loadBalancerFilter.loadBalancers(loadBalancers);
        return this;
    }

    public LoadBalancerPoolFilter loadBalancerPools(LoadBalancerPool... loadBalancerPools) {
        predicate = predicate.and(Filter.or(
                Streams.map(loadBalancerPools, LoadBalancerPool::asFilter)
        ).getPredicate());

        return this;
    }

    public LoadBalancerPoolFilter where(Predicate<LoadBalancerPoolMetadata> predicate) {
        this.predicate = this.predicate.and(predicate);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoadBalancerPoolFilter and(LoadBalancerPoolFilter otherFilter) {
        if (evaluation instanceof SingleFilterEvaluation &&
            otherFilter.evaluation instanceof SingleFilterEvaluation) {
            return
                new LoadBalancerPoolFilter(
                    getLoadBalancerFilter().and(otherFilter.getLoadBalancerFilter()),
                    getPredicate().and(otherFilter.getPredicate())
                );
        } else {
            evaluation = new AndEvaluation<>(evaluation, otherFilter, LoadBalancerPoolMetadata::getId);

            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoadBalancerPoolFilter or(LoadBalancerPoolFilter otherFilter) {
        evaluation = new OrEvaluation<>(evaluation, otherFilter, LoadBalancerPoolMetadata::getId);
        return this;
    }

    public LoadBalancerPoolFilter id(String... ids) {
        return id(asList(ids));
    }

    public LoadBalancerPoolFilter id(List<String> ids) {
        this.ids.addAll(ids);
        return this;
    }

    public List<String> getIds() {
        return ids;
    }

    public LoadBalancerFilter getLoadBalancerFilter() {
        return loadBalancerFilter;
    }

    public Predicate<LoadBalancerPoolMetadata> getPredicate() {
        return predicate;
    }
}
