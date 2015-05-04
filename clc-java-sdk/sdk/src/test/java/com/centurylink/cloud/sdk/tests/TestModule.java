package com.centurylink.cloud.sdk.tests;

import com.google.inject.AbstractModule;
import org.mockito.Mockito;

/**
 * @author Ilya Drabenia
 */
public abstract class TestModule extends AbstractModule {

    public <T> TestModule mock(Class<T> type) {
        bind(type).toInstance(Mockito.mock(type));
        return this;
    }

}
