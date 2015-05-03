package com.centurylink.cloud.sdk.core;

/**
 * @author Ilya Drabenia
 */
public interface CastMixin {

    default <T> T as(Class<T> type) {
        return type.cast(this);
    }

    default <T> boolean is(Class<T> type) {
        return type == getClass();
    }

}
