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

import com.centurylink.cloud.sdk.tests.TestModule;
import org.mockito.Spy;

import java.lang.reflect.Field;
import java.util.stream.Stream;

/**
 * @author Ilya Drabenia
 */
public class BindSpiesModule extends TestModule {
    private final Object target;

    public BindSpiesModule(Object target) {
        this.target = target;
    }

    @Override
    protected void configure() {
        overrideSpyBinding();
    }

    private void overrideSpyBinding() {
        Stream
            .of(declaredFields())
            .filter(f -> f.isAnnotationPresent(Spy.class))
            .forEach(f -> bind(f.getType())
                .toProvider(new SpyProvider<>(f.getType()))
        );
    }

    private Field[] declaredFields() {
        return target
            .getClass()
            .getDeclaredFields();
    }

}

