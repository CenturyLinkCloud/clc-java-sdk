package com.centurylink.cloud.sdk.tests.recorded;

import com.centurylink.cloud.sdk.core.client.SdkHttpClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;

import java.lang.reflect.Method;

import static com.centurylink.cloud.sdk.tests.recorded.WireMockFixture.fileSourcePath;
import static com.centurylink.cloud.sdk.tests.recorded.WireMockFixture.getWireMockServer;
import static com.centurylink.cloud.sdk.tests.recorded.WireMockFixture.setWireMockServer;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * @author Ilya Drabenia
 */
public interface WireMockMixin {

    default void initWireMock(Method method) {
        SdkHttpClient.apiUrl("http://localhost:8081/v2");

        setWireMockServer(new WireMockServer(wireMockConfig()
            .fileSource(new ClasspathFileSource(fileSourcePath(this, method)))
            .port(8081)
        ));

        getWireMockServer().start();
    }

    default void destroyWireMock() {
        SdkHttpClient.restoreUrl();

        getWireMockServer().stop();
    }

}
