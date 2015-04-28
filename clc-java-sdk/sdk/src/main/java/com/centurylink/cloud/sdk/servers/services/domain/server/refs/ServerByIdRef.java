package com.centurylink.cloud.sdk.servers.services.domain.server.refs;

import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;

/**
 * @author ilya.drabenia
 */
public class ServerByIdRef extends Server {
    private final String id;

    public ServerByIdRef(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public ServerFilter asFilter() {
        return new ServerFilter().id(id);
    }
}
