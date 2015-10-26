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

package com.centurylink.cloud.sdk.loadbalancer.services;

import com.centurylink.cloud.sdk.base.services.BaseModule;
import com.centurylink.cloud.sdk.core.injector.Module;
import com.centurylink.cloud.sdk.core.injector.bean.factory.BeanFactory;
import com.centurylink.cloud.sdk.loadbalancer.services.client.LoadBalancerClient;
import com.centurylink.cloud.sdk.loadbalancer.services.client.LoadBalancerNodeClient;
import com.centurylink.cloud.sdk.loadbalancer.services.client.LoadBalancerPoolClient;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerNodeService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerPoolService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerPoolService.LoadBalancerNodeServiceSupplier;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerService.LoadBalancerPoolServiceSupplier;

import java.util.Map;

public class LoadBalancerModule extends Module {

    @Override
    protected void configure() {
        install(new BaseModule());

        bind(LoadBalancerClient.class);
        bind(LoadBalancerPoolClient.class);
        bind(LoadBalancerNodeClient.class);
        bind(LoadBalancerService.class);
        bind(LoadBalancerPoolService.class);
        bind(LoadBalancerNodeService.class);
        bind(LoadBalancerPoolServiceSupplier.class, this::loadBalancerPoolServiceSupplier);
        bind(LoadBalancerNodeServiceSupplier.class, this::loadBalancerNodeServiceSupplier);
    }

    private LoadBalancerPoolServiceSupplier loadBalancerPoolServiceSupplier(Map<Class, BeanFactory> registry) {
        return () -> (LoadBalancerPoolService) registry.get(LoadBalancerPoolService.class).getBean(registry);
    }

    private LoadBalancerNodeServiceSupplier loadBalancerNodeServiceSupplier(Map<Class, BeanFactory> registry) {
        return () -> (LoadBalancerNodeService) registry.get(LoadBalancerNodeService.class).getBean(registry);
    }
}
