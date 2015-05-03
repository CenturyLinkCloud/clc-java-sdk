package com.centurylink.cloud.sdk.base.services;

/**
 * @author Ilya Drabenia
 */
public interface CastMixin {

    @SuppressWarnings("unchecked")
    default <T> T as(Class<T> type) {
        return (T) this;
    }

    default <T> boolean is(Class<T> type) {
        return type == getClass();
    }

}
