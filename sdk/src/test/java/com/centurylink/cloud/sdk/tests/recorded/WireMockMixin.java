package com.centurylink.cloud.sdk.tests.recorded;

import com.centurylink.cloud.sdk.core.client.SdkHttpClient;

import java.lang.reflect.Method;

/**
 * @author Ilya Drabenia
 */
public interface WireMockMixin {

    default void initWireMock(Method testingMethod) {
        if (this.getClass().getAnnotation(WireMockRecording.class) == null) {
            WireMockFixture.startServerFor(this, testingMethod);
        } else {
            SdkHttpClient.apiUrl("http://localhost:8089/v2");
        }
    }

    default void destroyWireMock() {
        WireMockFixture.stopServer();
    }

}
