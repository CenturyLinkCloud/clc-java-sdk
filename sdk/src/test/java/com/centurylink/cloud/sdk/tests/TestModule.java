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

package com.centurylink.cloud.sdk.tests;

import com.centurylink.cloud.sdk.core.injector.Module;
import org.mockito.Mockito;

/**
 * @author Ilya Drabenia
 */
public abstract class TestModule extends Module {

    public <T> TestModule mock(Class<T> type) {
        bindInstance(type, Mockito.mock(type));
        return this;
    }

}
