package com.centurylink.cloud.sdk.tests.mocks;

import com.centurylink.cloud.sdk.core.services.ClcServiceException;
import com.centurylink.cloud.sdk.servers.TestModule;
import com.google.inject.Binder;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Spy;

import java.lang.reflect.Field;
import java.util.stream.Stream;

/**
 * @author Ilya Drabenia
 */
public class BindSpiesModule extends TestModule {
    private final Object target;

    public BindSpiesModule(Object target) {
        this.target = target;
    }

    @Override
    protected void configure() {
        overrideSpyBinding();
    }

    private void overrideSpyBinding() {
        Stream
            .of(declaredFields())
            .filter(f -> f.isAnnotationPresent(Spy.class))
            .forEach(f -> bind(f.getType())
                .toProvider(new SpyProvider<>(f.getType()))
        );
    }

    private Field[] declaredFields() {
        return target
            .getClass()
            .getDeclaredFields();
    }

}

