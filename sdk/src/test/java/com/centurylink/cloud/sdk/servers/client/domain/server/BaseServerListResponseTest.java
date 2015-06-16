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

package com.centurylink.cloud.sdk.servers.client.domain.server;

import org.testng.annotations.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class BaseServerListResponseTest {
    BaseServerListResponse response;

    private BaseServerListResponse prepareResponseList(BaseServerResponse... responses) {
        return new BaseServerListResponse() {{
            add(new BaseServerResponse("server1", true, null, null));
            addAll(asList(responses));
        }};
    }

    private BaseServerResponse basicServerResponse(String server) {
        return new BaseServerResponse(server, null, null, null);
    }

    private String firstErrorMessage(List<Exception> errors) {
        return errors.get(0).getMessage();
    }

    @Test
    public void testListExceptions_multipleExceptions() throws Exception {
        response = prepareResponseList(
            basicServerResponse("VA1ALTDSRV101").queued(false).errorMessage("Job not started"),
            basicServerResponse("VA1ALTDSRV102").queued(null).errorMessage("Internal error occurs")
        );

        List<Exception> errors = response.listExceptions();

        assertEquals(errors.size(), 2);
    }

    @Test
    public void testListExceptions_emptyResult() throws Exception {
        response = prepareResponseList(basicServerResponse("VA1ALTDSRV101"));

        List<Exception> errors = response.listExceptions();

        assertEquals(errors.size(), 1);
        assertContains(firstErrorMessage(errors), "VA1ALTDSRV101", "not queued");
    }

    @Test
    public void testListExceptions_onlyErrorMessage() throws Exception {
        response = prepareResponseList(basicServerResponse("VA1ALTDSRV101").errorMessage("Something go wrong"));

        List<Exception> errors = response.listExceptions();

        assertEquals(errors.size(), 1);
        assertContains(firstErrorMessage(errors), "VA1ALTDSRV101", "Something go wrong");
    }

    @Test
    public void testListExceptions_onlyNotQueued() throws Exception {
        response = prepareResponseList(basicServerResponse("VA1ALTDSRV101").queued(false));

        List<Exception> errors = response.listExceptions();

        assertEquals(errors.size(), 1);
        assertContains(firstErrorMessage(errors), "VA1ALTDSRV101", "not queued");
    }

    private void assertContains(String str, String... keywords) {
        for (String curKeyword : keywords) {
            assertTrue(str.contains(curKeyword));
        }
    }

}