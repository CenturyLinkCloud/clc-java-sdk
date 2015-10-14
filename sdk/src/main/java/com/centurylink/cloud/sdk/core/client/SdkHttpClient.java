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

package com.centurylink.cloud.sdk.core.client;

import com.centurylink.cloud.sdk.core.client.errors.ErrorProcessingFilter;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author ilya.drabenia
 */
public class SdkHttpClient {
    private static final String INIT_API_DOMAIN = "https://api.ctl.io/";
    private static String API_DOMAIN = INIT_API_DOMAIN;

    protected static String CLC_API_URL;
    protected static String CLC_API_URL_EXPERIMENTAL;

    public static final SdkClientBuilder CLIENT_BUILDER =
        (SdkClientBuilder) ResteasyClientBuilder
            .newBuilder()
            .register(new ErrorProcessingFilter());

    static {
        updateApiUrl();
    }

    public static void apiUrl(String url) {
        API_DOMAIN = url;
        updateApiUrl();
    }

    public static void restoreUrl() {
        API_DOMAIN = INIT_API_DOMAIN;
        updateApiUrl();
    }

    private static void updateApiUrl() {
        CLC_API_URL = API_DOMAIN + "v2";
        CLC_API_URL_EXPERIMENTAL = API_DOMAIN + "v2-experimental";
    }

    public SdkHttpClient(SdkConfiguration config) {
        CLIENT_BUILDER
            .maxRetries(config.getMaxRetries())
            .proxyConfig(
                config.getProxyHost(),
                config.getProxyPort(),
                config.getProxyScheme(),
                config.getProxyUsername(),
                config.getProxyPassword()
            )
            .socketTimeout(config.getSocketTimeout(), MILLISECONDS);
    }

    protected ResteasyClient buildClient() {
        return CLIENT_BUILDER.build();
    }
}
