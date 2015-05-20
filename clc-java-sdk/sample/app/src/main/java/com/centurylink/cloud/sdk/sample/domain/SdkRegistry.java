/*
 * (c) 2015 CenturyLink Cloud. All Rights Reserved.
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
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.StaticCredentialsProvider;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author ilya.drabenia
 */
@Service
public class SdkRegistry {

    private ConcurrentMap<String, ClcSdk> sdkCache = new ConcurrentHashMap<>();

    public SdkRegistry() {
    }

    public ClcSdk findOrCreate(String username, String password) {
        if (sdkCache.get(username) == null) {
            sdkCache.putIfAbsent(username, new ClcSdk(new StaticCredentialsProvider(username, password)));
        }

        return sdkCache.get(username);
    }
}
