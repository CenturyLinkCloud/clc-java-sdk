package com.centurylink.cloud.sdk.servers.services.domain.server.refs;

import com.centurylink.cloud.sdk.core.services.refs.Reference;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;

/**
 * {@inheritDoc}
 */
public abstract class Server implements Reference<ServerFilter> {

    /**
     * Method allow to refer server by it's ID. Matching is case insensitive.
     * Comparison by full match.
     *
     * @param id is ID of needed server
     * @return {@link com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerByIdRef}
     */
    public static ServerByIdRef refById(String id) {
        return new ServerByIdRef(id);
    }

    @Override
    public String toString() {
        return this.toReadableString();
    }
}
