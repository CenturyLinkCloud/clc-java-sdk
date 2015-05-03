package com.centurylink.cloud.sdk.servers.services.domain.server.refs;

import com.centurylink.cloud.sdk.core.services.refs.Reference;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;

/**
 * @author ilya.drabenia
 */
public abstract class Server implements Reference {

    public static ServerByIdRef refById(String id) {
        return new ServerByIdRef(id);
    }

    @Override
    public abstract ServerFilter asFilter();

}
