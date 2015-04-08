package com.centurylink.cloud.sdk.tests.mocks;

import org.mockito.Mockito;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.mockito.Mockito.when;

/**
 * @author Ilya Drabenia
 */
public class Mocks {

    public static <T> MockBuilder<T> mock(Class<T> type) {
        return new MockBuilder<>(Mockito.mock(type));
    }

    public static <T> T construct(T object, Consumer<T> func) {
        func.accept(object);
        return object;
    }

    public static <T> void returnOn(Supplier<T> ongoingStubFunc, Supplier<T> actualResultFunc) {
        T actualResult = actualResultFunc.get();
        when(ongoingStubFunc.get()).thenReturn(actualResult);
    }

    public static <T> T newMock(Class<T> type, Consumer<T> config) {
        T mock = Mockito.mock(type);
        config.accept(mock);
        return mock;
    }

}
