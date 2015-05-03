package com.centurylink.cloud.sdk.core.services;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Ilya Drabenia
 */
public class SdkThreadPool {

    public static final Executor threadPool = Executors.newCachedThreadPool();

    public static Executor get() {
        return threadPool;
    }

}
