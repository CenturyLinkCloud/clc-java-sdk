package com.centurylink.cloud.sdk.servers.client.domain.server;

import java.util.ArrayList;
import java.util.Collection;

public class BaseServerListResponse extends ArrayList<CreateServerResponse> {

    public BaseServerListResponse(Collection<? extends CreateServerResponse> collection) {
        super(collection);
    }
}
