package com.centurylink.cloud.sdk.tests.mocks;

import org.mockito.stubbing.OngoingStubbing;

/**
 * @author Ilya Drabenia
 */
public class OngoingStubbingBuilder<T, R> {
    private final T mockedObject;
    private final OngoingStubbing<R> stubbing;

    public OngoingStubbingBuilder(T mockedObject, OngoingStubbing<R> stubbing) {
        this.mockedObject = mockedObject;
        this.stubbing = stubbing;
    }

    public OngoingStubbingBuilder<T, R> returns(R object) {
        return new OngoingStubbingBuilder<>(
            mockedObject,
            stubbing.thenReturn(object)
        );
    }

    public T instance() {
        return mockedObject;
    }
}
