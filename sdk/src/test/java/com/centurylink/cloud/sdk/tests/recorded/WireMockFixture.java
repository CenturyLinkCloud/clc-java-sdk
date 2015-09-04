package com.centurylink.cloud.sdk.tests.recorded;

import com.centurylink.cloud.sdk.core.client.SdkHttpClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;

import java.lang.reflect.Method;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

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
            return packageFolder(instance) + classSource.value();
        }

        WireMockFileSource methodSource = method.getAnnotation(WireMockFileSource.class);
        if (methodSource != null) {
            return packageFolder(instance) + methodSource.value();
        }

        return packageFolder(instance);
    }

    private static String packageFolder(Object instance) {
        return instance.getClass().getPackage().getName().replace(".", "/") + "/";
    }

    public static void startServerFor(Object instance, Method testMethod) {
        SdkHttpClient.apiUrl("http://localhost:8081/");

        setWireMockServer(new WireMockServer(wireMockConfig()
            .fileSource(new ClasspathFileSource(fileSourcePath(instance, testMethod)))
            .port(8081)
        ));

        getWireMockServer().start();
    }

    public static void stopServer() {
        SdkHttpClient.restoreUrl();

        if (getWireMockServer() != null && getWireMockServer().isRunning()) {
            getWireMockServer().stop();
        }
    }
}
