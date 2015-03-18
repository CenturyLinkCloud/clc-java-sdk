package com.centurylink.cloud.sdk.servers.services.domain.server.refs;

/**
 * @author ilya.drabenia
 */
public class IdServerRef extends ServerRef {
    private final String id;

    public IdServerRef(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
