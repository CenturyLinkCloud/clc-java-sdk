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

package sample;

import com.centurylink.cloud.sdk.EnableClcSdk;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import com.centurylink.cloud.sdk.core.config.SdkConfigurationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableClcSdk
public class CustomSdkConfigurationConfig {

    public static final Integer MAX_RETRIES = 10;
    public static final String PROXY_HOST = "127.0.0.1";

    @Bean
    public SdkConfiguration clcSdkConfig() {
        return
            new SdkConfigurationBuilder()
                .maxRetries(MAX_RETRIES)
                .proxyHost(PROXY_HOST)
                .build();
    }

}
