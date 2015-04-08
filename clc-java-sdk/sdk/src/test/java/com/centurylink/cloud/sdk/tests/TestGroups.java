package com.centurylink.cloud.sdk.tests;

/**
 * @author Ilya Drabenia
 */
public interface TestGroups {
    /**
     * Group that contains tests required access to CenturyLink Cloud API
     */
    static String INTEGRATION = "Integration";

    /**
     * Group that execution time do not allow run such tests on application build
     */
    static String LONG_RUNNING = "LongRunning";
}
