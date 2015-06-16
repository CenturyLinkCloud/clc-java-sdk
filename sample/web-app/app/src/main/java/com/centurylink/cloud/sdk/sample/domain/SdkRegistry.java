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

package com.centurylink.cloud.sdk.sample.domain;

import com.centurylink.cloud.sdk.ClcSdk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author ilya.drabenia
 */
@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SdkRegistry {

    @Autowired
    private SdkCache cache;

    private volatile SdkCredentials credentials;

    public SdkRegistry() {
    }

    private Map<String, ClcSdk> cacheMap() {
        return cache.getMap();
    }

    public ClcSdk getSdk() {
        return cache.findOrCreate(credentials);
    }

    public void setCredentials(SdkCredentials credentials) {
        this.credentials = credentials;
    }
}
