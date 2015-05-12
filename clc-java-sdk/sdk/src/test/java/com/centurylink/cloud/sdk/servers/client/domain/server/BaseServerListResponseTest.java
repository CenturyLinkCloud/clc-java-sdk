package com.centurylink.cloud.sdk.servers.client.domain.server;

import org.testng.annotations.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.testng.Assert.*;

public class BaseServerListResponseTest {

    private BaseServerListResponse prepareResponseList(BaseServerResponse... responses) {
        return new BaseServerListResponse() {{
            add(new BaseServerResponse("server1", true, null, null));
            addAll(asList(responses));
        }};
    }

    private BaseServerResponse basicServerResponse(String server) {
        return new BaseServerResponse(server, null, null, null);
    }

    @Test
    public void testListExceptions_multipleExceptions() throws Exception {
        BaseServerListResponse response = prepareResponseList(
            basicServerResponse("server2").queued(false).errorMessage("Error Message"),
            basicServerResponse("server3").queued(null).errorMessage("Error Message")
        );

        List<Exception> errors = response.listExceptions();

        assertEquals(errors.size(), 2);
    }

    @Test
    public void testListExceptions_emptyResult() throws Exception {
        BaseServerListResponse response = prepareResponseList(
            basicServerResponse("server2")
        );

        List<Exception> errors = response.listExceptions();

        assertEquals(errors.size(), 1);
        assertContains(firstErrorMessage(errors), "server2", "not queued");
    }

    private String firstErrorMessage(List<Exception> errors) {
        return errors.get(0).getMessage();
    }

    @Test
    public void testListExceptions_onlyErrorMessage() throws Exception {
        BaseServerListResponse response = prepareResponseList(
            basicServerResponse("server2").errorMessage("Error Message")
        );

        List<Exception> errors = response.listExceptions();

        assertEquals(errors.size(), 1);
        assertContains(firstErrorMessage(errors), "server2", "Error Message");
    }

    @Test
    public void testListExceptions_onlyNotQueued() throws Exception {
        BaseServerListResponse response = prepareResponseList(
            basicServerResponse("server2").queued(false)
        );

        List<Exception> errors = response.listExceptions();

        assertEquals(errors.size(), 1);
        assertContains(firstErrorMessage(errors), "server2", "not queued");
    }

    private void assertContains(String str, String... keywords) {
        for (String curKeyword : keywords) {
            assertTrue(str.contains(curKeyword));
        }
    }

}