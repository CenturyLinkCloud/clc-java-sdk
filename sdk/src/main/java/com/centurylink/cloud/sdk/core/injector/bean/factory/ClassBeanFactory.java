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

package com.centurylink.cloud.sdk.core.injector.bean.factory;

import com.centurylink.cloud.sdk.core.exceptions.ClcException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ilya Drabenia
 */
public class ClassBeanFactory implements BeanFactory {
    private final AtomicReference<Object> instance = new AtomicReference<>();
    private final Class type;

    public ClassBeanFactory(Class typeVal) {
        type = typeVal;
    }

    @Override
    public Object getBean(Map<Class, BeanFactory> registryVal) {
        if (instance.get() == null) {
            instance.set(createBean(registryVal));
        }

        return instance.get();
    }

    private Object createBean(Map<Class, BeanFactory> registry) {
        Constructor constructor = type.getDeclaredConstructors()[0];
        Class[] paramTypes = constructor.getParameterTypes();

        Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            BeanFactory factory = registry.get(paramTypes[i]);

            if (factory == null) {
                registry.put(paramTypes[i], new ClassBeanFactory(paramTypes[i]));
            }

            params[i] = registry.get(paramTypes[i]).getBean(registry);
        }

        try {
            return params.length > 0 ? constructor.newInstance(params) : constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException
            | InvocationTargetException e) {
            throw new ClcException(e);
        }
    }
}
