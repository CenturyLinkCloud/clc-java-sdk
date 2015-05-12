package com.centurylink.cloud.sdk.servers.client.domain.server;

import org.testng.annotations.Test;

import java.util.Arrays;
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

    @Test
    public void testListExceptions_multipleExceptions() throws Exception {
        BaseServerListResponse response = prepareResponseList(
            new BaseServerResponse("server2", false, null, "Error Message"),
            new BaseServerResponse("server3", null, null, "Error Message")
        );

        List<Exception> errors = response.listExceptions();

        assertEquals(errors.size(), 2);
    }

    @Test
    public void testListExceptions_emptyResult() throws Exception {
        BaseServerListResponse response = prepareResponseList(
            new BaseServerResponse("server2", null, null, null)
        );

        List<Exception> errors = response.listExceptions();

        assertEquals(errors.size(), 1);
        assertExceptionMessageContains(errors.get(0), "server2", "not queued");
    }

    @Test
    public void testListExceptions_onlyErrorMessage() throws Exception {
        BaseServerListResponse response = prepareResponseList(
            new BaseServerResponse("server2", null, null, "Error Message")
        );

        List<Exception> errors = response.listExceptions();

        assertEquals(errors.size(), 1);
        assertExceptionMessageContains(errors.get(0), "server2", "Error Message");
    }

    @Test
    public void testListExceptions_onlyNotQueued() throws Exception {
        BaseServerListResponse response = prepareResponseList(
            new BaseServerResponse("server2", false, null, null)
        );

        List<Exception> errors = response.listExceptions();

        assertEquals(errors.size(), 1);
        assertExceptionMessageContains(errors.get(0), "server2", "not queued");
    }

    private void assertExceptionMessageContains(Exception ex, String... keywords) {
        for (String curKeyword : keywords) {
            assertTrue(ex.getMessage().contains(curKeyword));
        }
    }

}