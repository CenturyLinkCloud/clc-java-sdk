package com.centurylink.cloud.sdk.servers.client.domain.server;

import java.util.ArrayList;
import java.util.Collection;

public class BaseServerListResponse extends ArrayList<BaseServerResponse> {

    public BaseServerListResponse(Collection<? extends BaseServerResponse> collection) {
        super(collection);
    }
}
