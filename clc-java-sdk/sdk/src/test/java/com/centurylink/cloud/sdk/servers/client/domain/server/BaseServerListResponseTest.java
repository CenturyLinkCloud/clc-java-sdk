package com.centurylink.cloud.sdk.servers.client.domain.server;

import org.testng.annotations.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.testng.Assert.*;

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