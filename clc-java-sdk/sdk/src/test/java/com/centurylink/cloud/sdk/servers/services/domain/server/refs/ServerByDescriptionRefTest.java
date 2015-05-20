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

package com.centurylink.cloud.sdk.servers.services.domain.server.refs;

import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.US_WEST_SANTA_CLARA;
import static org.testng.Assert.assertTrue;

public class ServerByDescriptionRefTest {

    @Test
    public void testServerByDescSerialization() {
        Server server = Server.refByDescription(US_WEST_SANTA_CLARA, "My Description");

        String toStringValue = server.toString();

        assertTrue(toStringValue.contains("My Description"));
        assertTrue(toStringValue.contains("uc1"));
    }

}