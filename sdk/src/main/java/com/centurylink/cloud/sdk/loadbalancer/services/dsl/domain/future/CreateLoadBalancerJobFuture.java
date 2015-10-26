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

package com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.future;

import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.AbstractSingleJobFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.waiting.SingleWaitingLoop;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.waiting.WaitingLoop;
import com.centurylink.cloud.sdk.core.exceptions.ClcException;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.group.LoadBalancer;

/**
 * @author Aliaksandr Krasitski
 */
public class CreateLoadBalancerJobFuture extends AbstractSingleJobFuture {
    private final LoadBalancerService loadBalancerService;
    private final LoadBalancer loadBalancer;

    public CreateLoadBalancerJobFuture(LoadBalancerService loadBalancerService, LoadBalancer loadBalancer) {
        this.loadBalancerService = loadBalancerService;
        this.loadBalancer = loadBalancer;
    }

    @Override
    protected String operationInfo() {
        return null;
    }

    @Override
    public WaitingLoop waitingLoop() {
        return
            new SingleWaitingLoop(() -> {
                try {
                    loadBalancerService.findByRef(loadBalancer);
                    return true;
                } catch (ClcException e) {
                    return false;
                }
            });
    }
}
