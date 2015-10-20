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

package com.centurylink.cloud.sdk.core.injector;

import com.centurylink.cloud.sdk.core.injector.bean.factory.BeanFactory;
import com.centurylink.cloud.sdk.core.injector.bean.factory.ClassBeanFactory;
import com.centurylink.cloud.sdk.core.injector.bean.factory.InstanceBeanFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ilya Drabenia
 */
public abstract class Module {
    private final Map<Class, BeanFactory> registry = new HashMap<>();
    private final List<Module> dependentModules = new ArrayList<>();

    protected abstract void configure();

    protected <T> void bind(Class<T> type) {
        registry.put(type, new ClassBeanFactory(type));
    }

    protected <T> void bind(Class<T> type, Object instance) {
        registry.put(type, new InstanceBeanFactory(instance));
    }

    protected <T> void bind(Class<T> type, BeanFactory beanFactory) {
        registry.put(type, beanFactory);
    }

    protected void install(Module module) {
        dependentModules.add(module);
    }

    public Map<Class, BeanFactory> getRegistry() {
        return registry;
    }

    public List<Module> getDependentModules() {
        return dependentModules;
    }

    public List<Module> getAllModules() {
        return new ArrayList<Module>() {{
            addAll(dependentModules);
            add(Module.this);
        }};
    }
}
