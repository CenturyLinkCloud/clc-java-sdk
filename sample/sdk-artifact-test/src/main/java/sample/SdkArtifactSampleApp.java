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

import com.centurylink.cloud.sdk.ClcSdk;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.CredentialsProvider;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.DefaultCredentialsProvider;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.Assert;
import org.testng.annotations.Test;

import static sample.TestGroup.ARTIFACT_TEST;

public class SdkArtifactSampleApp extends Assert {

    private void assertSdkAvailability() {
        ClcSdk sdk = new ClcSdk(
            new DefaultCredentialsProvider()
        );

        assertNotNull(sdk);
    }

    private void assertSdkAvailabilityFromSpringContext() {
        ApplicationContext context = new AnnotationConfigApplicationContext(
            CustomCredentialsProviderConfig.class
        );

        checkCredentials(
            context
                .getBean(ClcSdk.class)
                .getCredentialsProvider()
        );

        context = new AnnotationConfigApplicationContext(
            CustomSdkConfigurationConfig.class
        );

        checkConfiguration(context.getBean(SdkConfiguration.class));
    }

    private void checkCredentials(CredentialsProvider provider) {
        assertEquals(
            provider.getCredentials().getPassword(),
            CustomCredentialsProviderConfig.PASSWORD
        );

        assertEquals(
            provider.getCredentials().getUsername(),
            CustomCredentialsProviderConfig.USERNAME
        );
    }

    private void checkConfiguration(SdkConfiguration config) {
        assertEquals(config.getMaxRetries(), CustomSdkConfigurationConfig.MAX_RETRIES);
        assertEquals(config.getProxyHost(), CustomSdkConfigurationConfig.PROXY_HOST);
    }

    @Test(groups = {ARTIFACT_TEST})
    public void runTest() {
        assertSdkAvailability();
        assertSdkAvailabilityFromSpringContext();
    }
}
