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

import com.centurylink.cloud.sdk.core.exceptions.ClcException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static javax.ws.rs.core.HttpHeaders.USER_AGENT;

public class UserAgentRequestFilter implements ClientRequestFilter {

    private static String userAgent;

    static {
        fetchUserAgentFromProperties();
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        requestContext.getHeaders().putSingle(USER_AGENT, userAgent);
    }

    private static void fetchUserAgentFromProperties() {
        Properties properties = new Properties();

        try (
            InputStream stream = UserAgentRequestFilter.class
                .getClassLoader()
                .getResourceAsStream("config.properties")
        ) {
            properties.load(stream);
            userAgent = properties.getProperty("artifactId") + "-v" + properties.getProperty("version");
        } catch (IOException ex) {
            throw new ClcException("Can't load user agent data from config.properties");
        }
    }
}
