package com.centurylink.cloud.sdk.tests.recorded;

import com.centurylink.cloud.sdk.core.exceptions.ClcException;
import com.github.tomakehurst.wiremock.WireMockServer;

import java.lang.reflect.Method;

/**
 * @author Ilya Drabenia
 */
public class WireMockFixture {
    private static volatile WireMockServer wireMockServer;

    public static WireMockServer getWireMockServer() {
        return wireMockServer;
    }

    public static void setWireMockServer(WireMockServer wireMockServer) {
        WireMockFixture.wireMockServer = wireMockServer;
    }

    static String fileSourcePath(Object instance, Method method) {
        WireMockFileSource classSource = instance.getClass().getAnnotation(WireMockFileSource.class);

        if (classSource != null) {
            return instance.getClass().getPackage().getName().replace(".", "/") + "/" + classSource.value();
        }

        WireMockFileSource methodSource = method.getAnnotation(WireMockFileSource.class);
        if (methodSource != null) {
            return instance.getClass().getPackage().getName().replace(".", "/") + "/" + methodSource.value();
        }

        throw new ClcException("WireMock File Source not specified");
    }
}
