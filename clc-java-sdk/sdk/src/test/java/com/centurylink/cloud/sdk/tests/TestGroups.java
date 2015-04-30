package com.centurylink.cloud.sdk.tests;

/**
 * @author Ilya Drabenia
 */
public interface TestGroups {
    /**
     * Group that contains tests required access to CenturyLink Cloud API
     */
    String INTEGRATION = "Integration";

    /**
     * Group that execution time do not allow run such tests on application build
     */
    String LONG_RUNNING = "LongRunning";

    /**
     * Group of tests that contains real business scenarios
     */
    String SAMPLES = "Samples";
}
