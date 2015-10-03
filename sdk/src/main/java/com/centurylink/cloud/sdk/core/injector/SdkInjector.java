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


import com.centurylink.cloud.sdk.core.exceptions.ClcException;
import com.centurylink.cloud.sdk.core.injector.bean.factory.BeanFactory;
import com.centurylink.cloud.sdk.core.injector.bean.factory.ClassBeanFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

/**
 * @author Ilya Drabenia
 */
public class SdkInjector {
    private final Map<Class, BeanFactory> registry = new HashMap<>();
    private final List<Module> modules;

    public SdkInjector(List<Module> modulesValue) {
        modules =
            modulesValue.stream()
                .flatMap(m -> m.getAllModules().stream())
                .collect(toList());

        modules.forEach(Module::configure);

        modules.stream()
            .map(Module::getRegistry)
            .forEach(registry::putAll);
    }

    public static SdkInjector createInjector(List<Module> modules) {
        return new SdkInjector(modules);
    }

    public static SdkInjector createInjector(Module... modules) {
        return new SdkInjector(asList(modules));
    }

    public <T> T getInstance(Class<T> type) {
        return (T) registry.get(type).getBean(registry);
    }

    public void injectMembers(Object target) {
        declaredFields(target).stream()
            .filter(f -> f.isAnnotationPresent(Inject.class))
            .forEach(field -> injectBeanToField(target, field));
    }

    private void injectBeanToField(Object target, Field field) {
        field.setAccessible(true);
        BeanFactory factory = registry.get(field.getType());

        if (factory == null) {
            factory = new ClassBeanFactory(field.getType());
        }

        try {
            field.set(target, factory.getBean(registry));
        } catch (IllegalAccessException e) {
            throw new ClcException(e);
        }
    }

    private List<Field> declaredFields(Object target) {
        return declaredFields(target.getClass());
    }

    private List<Field> declaredFields(Class targetType) {
        if (targetType != Object.class) {
            return list(
                targetType.getDeclaredFields(),
                declaredFields(targetType.getSuperclass())
            );
        } else {
            return new ArrayList<>();
        }
    }

    private List<Field> list(Field[] fields, List<Field> otherFields) {
        return new ArrayList<Field>() {{
            addAll(asList(fields));
            addAll(otherFields);
        }};
    }

}
