package com.centurylink.cloud.sdk.core.datacenters.services;

import com.centurylink.cloud.sdk.core.datacenters.client.DataCentersClient;
import com.centurylink.cloud.sdk.core.exceptions.ClcException;
import com.centurylink.cloud.sdk.core.exceptions.ResourceNotFoundException;
import com.centurylink.cloud.sdk.core.datacenters.client.domain.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenter;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.DataCenterRef;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.IdDataCenterRef;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.NameDataCenterRef;
import com.google.inject.Inject;

/**
 * @author ilya.drabenia
 */
public class DataCenterService {
    private final DataCentersClient serverClient;

    @Inject
    public DataCenterService(DataCentersClient serverClient) {
        this.serverClient = serverClient;
    }

    public DataCenter resolveId(DataCenter dataCenter) {
        if (dataCenter.getId() != null) {
            return dataCenter;
        }

        DataCenterMetadata result = serverClient
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

    public DataCenterMetadata findByRef(DataCenterRef dataCenterRef) {
        DataCenterMetadata result = null;
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
            return result;
        } else {
            throw new ResourceNotFoundException("Data center not resolved");
        }
    }
}
