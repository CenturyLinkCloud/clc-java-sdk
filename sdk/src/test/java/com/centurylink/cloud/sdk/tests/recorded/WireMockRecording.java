package com.centurylink.cloud.sdk.tests.recorded;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Ilya Drabenia
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface WireMockRecording {
}
