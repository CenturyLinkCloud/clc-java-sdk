package com.centurylink.cloud.sdk.servers.services.domain.server.refs;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.services.refs.Reference;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.google.common.base.Preconditions;

import static com.google.common.base.Preconditions.checkNotNull;

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

    public static ServerByDescriptionRef refByDescription(DataCenter dataCenter, String keyword) {
        checkNotNull(dataCenter, "Datacenter must be not null");
        checkNotNull(keyword, "Keyword must be not null");

        return new ServerByDescriptionRef(dataCenter, keyword);
    }

    public static ServerByDescriptionRef refByDescription(String keyword) {
        checkNotNull(keyword, "Keyword must be not null");

        return new ServerByDescriptionRef(null, keyword);
    }

    @Override
    public String toString() {
        return this.toReadableString();
    }
}
