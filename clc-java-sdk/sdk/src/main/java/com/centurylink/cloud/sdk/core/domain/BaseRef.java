package com.centurylink.cloud.sdk.core.domain;

/**
 * @author ilya.drabenia
 */
public interface BaseRef {

    @SuppressWarnings("unchecked")
    default <T extends BaseRef> T as(Class<T> type) {
        return (T) this;
    }

    default <T extends BaseRef> boolean is(Class<T> type) {
        return type == getClass();
    }

}
