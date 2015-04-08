package com.centurylink.cloud.sdk.tests.mocks;

import com.centurylink.cloud.sdk.core.services.ClcServiceException;
import com.centurylink.cloud.sdk.servers.TestModule;
import com.centurylink.cloud.sdk.tests.AbstractSdkTest;
import org.mockito.Mock;

import java.lang.reflect.Field;
import java.util.stream.Stream;

/**
 * @author Ilya Drabenia
 */
public class BindInjectedMocksModule extends TestModule {
    private final Object target;

    public BindInjectedMocksModule(Object target) {
        this.target = target;
    }

    @Override
    protected void configure() {
        overrideInjectedMocksBinding();
    }

    @SuppressWarnings("unchecked")
    private void overrideInjectedMocksBinding() {
        Stream
            .of(declaredFields())
            .filter(f -> f.isAnnotationPresent(Mock.class))
            .forEach(f -> bind((Class<Object>) f.getType()).toInstance(fieldValue(f)));
    }

    private Field[] declaredFields() {
        return target
                .getClass()
                .getDeclaredFields();
    }

    private Object fieldValue(Field f) {
        try {
            f.setAccessible(true);
            return f.get(target);
        } catch (IllegalAccessException e) {
            throw new ClcServiceException("Could not access field value", e);
        }
    }

}
