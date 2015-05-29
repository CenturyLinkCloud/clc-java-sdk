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

import com.centurylink.cloud.sdk.core.client.ClcClientException;
import com.centurylink.cloud.sdk.tests.mocks.BindMocksModule;
import com.centurylink.cloud.sdk.tests.mocks.BindSpiesModule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * @author ilya.drabenia
 */
public abstract class AbstractSdkTest extends Assert {

    @BeforeClass(alwaysRun = true)
    @BeforeMethod(alwaysRun = true)
    public void injectDependencies() {
        Guice
            .createInjector(composeModules())
            .injectMembers(this);
    }

    private Module composeModules() {
        return Modules.override(modules()).with(
            new BindMocksModule(this),
            new BindSpiesModule(this)
        );
    }

    protected List<Module> list(Module... modules) {
        return asList(modules);
    }

    protected abstract List<Module> modules();


    public <T> T fromJson(String filePath, Class<T> type) {
        try {
            return
                new ObjectMapper().readValue(
                    getClass().getResourceAsStream(filePath), type
                );
        } catch (IOException e) {
            throw new ClcClientException(e);
        }
    }

    public <T> T fromJson(String filePath, TypeReference<T> type) {
        try {
            return
                new ObjectMapper().readValue(
                    getClass().getResourceAsStream(filePath), type
                );
        } catch (IOException e) {
            throw new ClcClientException(e);
        }
    }

}
