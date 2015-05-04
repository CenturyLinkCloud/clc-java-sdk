package com.centurylink.cloud.sdk.servers.services.domain.server.refs;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;

import static com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions.notNull;

/**
 * @author Ilya Drabenia
 */
public class ServerByDescriptionRef extends Server {
    private final DataCenter dataCenter;
    private final String keyword;

    public ServerByDescriptionRef(DataCenter dataCenter, String keyword) {
        this.dataCenter = dataCenter;
        this.keyword = keyword;
    }

    @Override
    public ServerFilter asFilter() {
        return
            new ServerFilter() {{
                if (dataCenter != null) {
                    dataCenters(dataCenter);
                }

                descriptionContains(keyword);
            }};
    }

    public ServerByDescriptionRef dataCenter(DataCenter dataCenter) {
        notNull(dataCenter, "Data center must be not null");

        return new ServerByDescriptionRef(dataCenter, this.keyword);
    }

    public ServerByDescriptionRef keyword(String keyword) {
        notNull(keyword, "Keyword must be not null");

        return new ServerByDescriptionRef(this.dataCenter, keyword);
    }
}
