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

package com.centurylink.cloud.sdk.core.config;

import com.google.inject.AbstractModule;

/**
 * @author Ilya Drabenia
 */
public class SdkConfiguration {
    private final Integer maxRetries;
    private String proxyHost;
    private int proxyPort = -1;
    private String proxyScheme;
    private String proxyUsername;
    private String proxyPassword;

    public static final SdkConfiguration DEFAULT = new SdkConfiguration();

    SdkConfiguration() {
        this(new SdkConfigurationBuilder());
    }

    SdkConfiguration(SdkConfigurationBuilder builder) {
        maxRetries = builder.getMaxRetries();
        proxyHost = builder.getProxyHost();
        proxyPort = builder.getProxyPort();
        proxyScheme = builder.getProxyScheme();
        proxyUsername = builder.getProxyUsername();
        proxyPassword = builder.getProxyPassword();
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public String getProxyScheme() {
        return proxyScheme;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public AbstractModule asModule() {
        return new AbstractModule() {
            @Override
            protected void configure() {
                bind(SdkConfiguration.class).toInstance(SdkConfiguration.this);
            }
        };
    }

    public static SdkConfigurationBuilder builder() {
        return new SdkConfigurationBuilder();
    }

}
