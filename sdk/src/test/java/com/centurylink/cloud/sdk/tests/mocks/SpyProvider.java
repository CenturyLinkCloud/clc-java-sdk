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

package com.centurylink.cloud.sdk.tests.mocks;

import com.centurylink.cloud.sdk.core.exceptions.ClcException;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.HasDependencies;
import com.google.inject.spi.InjectionPoint;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.Mockito.spy;

/**
 * @author Ilya Drabenia
 */
class SpyProvider<T> implements Provider<T>, HasDependencies {

    private final InjectionPoint constructorInjectionPoint;
    private final Constructor<T> constructor;
    private final Set<Dependency<?>> dependencySet;
    private final AtomicReference<T> cachedValue = new AtomicReference<>();

    @Inject private Injector injector;  // Guice will automatically inject this when the injector is created

    /**
     * Construct a {@link Provider} that will return spied instances of objects
     * of the specified types.
     *
     * @param typeToProvide The {@link TypeLiteral} of the spy object to provide.
     */
    @SuppressWarnings("unchecked")
    public SpyProvider(Class<?> typeToProvide) {
        constructorInjectionPoint = InjectionPoint.forConstructorOf(typeToProvide);
        constructor = (Constructor<T>) constructorInjectionPoint.getMember();
        dependencySet = new HashSet<Dependency<?>>(constructorInjectionPoint.getDependencies());
        addDependenciesForMethodsAndFields(typeToProvide);
    }

    private void addDependenciesForMethodsAndFields(Class<?> typeToProvide) {
        Set<InjectionPoint> injectionPoints = InjectionPoint.forInstanceMethodsAndFields(typeToProvide);
        for (InjectionPoint injectionPoint : injectionPoints) {
            dependencySet.addAll(injectionPoint.getDependencies());
        }
    }

    @Override
    public T get() {
        if (cachedValue.get() == null) {
            List<Dependency<?>> dependencies = constructorInjectionPoint.getDependencies();

            Object[] constructorParameters = new Object[dependencies.size()];
            for (Dependency<?> dependency : dependencies) {
                constructorParameters[dependency.getParameterIndex()] =
                    injector.getInstance(dependency.getKey());
            }

            T instance;
            try {
                instance = constructor.newInstance(constructorParameters);
            } catch (Exception e) {
                throw new ClcException(e);
            }

            injector.injectMembers(instance);

            cachedValue.compareAndSet(null, spy(instance));
        }

        return cachedValue.get();
    }

    @Override
    public Set<Dependency<?>> getDependencies() {
        return dependencySet;
    }
}