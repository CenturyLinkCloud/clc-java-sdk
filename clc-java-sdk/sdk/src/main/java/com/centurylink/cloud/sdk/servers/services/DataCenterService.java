package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.core.exceptions.ResourceNotFoundException;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.datacenter.GetDataCenterResponse;
import com.centurylink.cloud.sdk.servers.services.domain.datacenter.DataCenter;
import com.google.inject.Inject;

/**
 * @author ilya.drabenia
 */
public class DataCenterService {
    private final ServerClient serverClient;

    @Inject
    public DataCenterService(ServerClient serverClient) {
        this.serverClient = serverClient;
    }

    public DataCenter resolveId(DataCenter dataCenter) {
        if (dataCenter.getId() != null) {
            return dataCenter;
        }

        GetDataCenterResponse result = serverClient
            .findAllDataCenters()
            .findWhereNameContains(dataCenter.getName());

        if (result != null) {
            return
                new DataCenter()
                    .id(result.getId())
                    .name(result.getName());
        } else {
            throw new ResourceNotFoundException("Data center [" + dataCenter.getName() + "] not resolved");
        }
    }
}
