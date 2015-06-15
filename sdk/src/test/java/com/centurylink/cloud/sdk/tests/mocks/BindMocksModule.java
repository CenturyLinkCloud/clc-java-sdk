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

import com.centurylink.cloud.sdk.core.services.ClcServiceException;
import com.centurylink.cloud.sdk.tests.TestModule;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.stream.Stream;

/**
 * @author Ilya Drabenia
 */
public class BindMocksModule extends TestModule {
    private final Object target;

    public BindMocksModule(Object target) {
        this.target = target;
    }

    @Override
    protected void configure() {
        overrideInjectedMocksBinding();
    }

    @SuppressWarnings("unchecked")
    private void overrideInjectedMocksBinding() {
        Stream
            .of(declaredFields())
            .filter(f -> f.isAnnotationPresent(Mock.class))
            .forEach(f -> bind((Class<Object>) f.getType()).toInstance(fieldValue(f)));
    }

    private Field[] declaredFields() {
        return target
            .getClass()
            .getDeclaredFields();
    }

    private Object fieldValue(Field f) {
        try {
            f.setAccessible(true);
            return (f.get(target) != null) ? f.get(target) : Mockito.mock(f.getType());
        } catch (IllegalAccessException e) {
            throw new ClcServiceException("Could not access field value", e);
        }
    }

}
