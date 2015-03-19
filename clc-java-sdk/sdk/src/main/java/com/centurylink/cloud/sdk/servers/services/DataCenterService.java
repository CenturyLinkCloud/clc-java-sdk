package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.core.exceptions.ClcException;
import com.centurylink.cloud.sdk.core.exceptions.ResourceNotFoundException;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.core.datacenters.client.domain.GetDataCenterResponse;
import com.centurylink.cloud.sdk.servers.services.domain.datacenter.DataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.datacenter.refs.DataCenterRef;
import com.centurylink.cloud.sdk.servers.services.domain.datacenter.refs.IdDataCenterRef;
import com.centurylink.cloud.sdk.servers.services.domain.datacenter.refs.NameDataCenterRef;
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

    public DataCenter findByRef(DataCenterRef dataCenterRef) {
        GetDataCenterResponse result = null;
        if (dataCenterRef.is(IdDataCenterRef.class)) {
            result = serverClient
                .findAllDataCenters()
                .findById(
                    dataCenterRef.as(IdDataCenterRef.class).getId()
                );
        } else if (dataCenterRef.is(NameDataCenterRef.class)) {
            result = serverClient
                .findAllDataCenters()
                .findWhereNameContains(dataCenterRef.as(NameDataCenterRef.class).getName());
        } else {
            throw new ClcException("Current type " +
                "[" + dataCenterRef.getClass().getSimpleName() + "]" +
                " of Data Center Reference is not supported");
        }

        if (result != null) {
            return
                new DataCenter()
                    .id(result.getId())
                    .name(result.getName());
        } else {
            throw new ResourceNotFoundException("Data center not resolved");
        }
    }
}
