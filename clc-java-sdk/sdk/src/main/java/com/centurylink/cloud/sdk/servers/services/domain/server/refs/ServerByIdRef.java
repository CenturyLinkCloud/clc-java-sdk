package com.centurylink.cloud.sdk.servers.services.domain.server.refs;

import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;

import java.util.Objects;

/**
 * @author ilya.drabenia
 */
public class ServerByIdRef extends Server {
    private final String id;

    ServerByIdRef(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public ServerFilter asFilter() {
        return new ServerFilter().id(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerByIdRef that = (ServerByIdRef) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
