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

package com.centurylink.cloud.sdk.core.client;

import com.centurylink.cloud.sdk.core.client.errors.ErrorProcessingFilter;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

/**
 * @author ilya.drabenia
 */
public class SdkHttpClient {
    public static final String CLC_API_URL = "https://api.tier3.com/v2";

    public static final SdkClientBuilder CLIENT_BUILDER =
        (SdkClientBuilder) ResteasyClientBuilder
            .newBuilder()
            .register(new ErrorProcessingFilter());

    public SdkHttpClient(SdkConfiguration config) {
        CLIENT_BUILDER.maxRetries(config.getMaxRetries());
        CLIENT_BUILDER.proxyConfig(
            config.getProxyHost(),
            config.getProxyPort(),
            config.getProxyScheme(),
            config.getProxyUsername(),
            config.getProxyPassword()
        );
    }

    protected ResteasyClient buildClient() {
        return CLIENT_BUILDER.build();
    }
}
