package com.centurylink.cloud.sdk.servers.services.domain.server.refs;

import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;

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

    @Override
    public ServerFilter asFilter() {
        return new ServerFilter().idIn(id);
    }
}
