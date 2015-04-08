package com.centurylink.cloud.sdk.tests.mocks;

import java.util.function.Function;

import static org.mockito.Mockito.when;

/**
 * @author Ilya Drabenia
 */
public class MockBuilder<T> {
    private final T mockedObject;

    public MockBuilder(T mockedObject) {
        this.mockedObject = mockedObject;
    }

    public <K> OngoingStubbingBuilder<T, K> on(Function<T, K> func) {
        return new OngoingStubbingBuilder<>(
            mockedObject,
            when(func.apply(mockedObject))
        );
    }
}
