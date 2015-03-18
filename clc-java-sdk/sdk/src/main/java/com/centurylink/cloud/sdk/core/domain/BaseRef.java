package com.centurylink.cloud.sdk.core.domain;

/**
 * @author ilya.drabenia
 */
public class BaseRef {
    public <T extends BaseRef> T as(Class<T> type) {
        return (T) this;
    }

    public <T extends BaseRef> boolean is(Class<T> type) {
        return type == getClass();
    }
}
