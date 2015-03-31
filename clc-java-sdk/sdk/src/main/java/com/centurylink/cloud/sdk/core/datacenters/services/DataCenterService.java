package com.centurylink.cloud.sdk.core.datacenters.services;

import com.centurylink.cloud.sdk.core.datacenters.client.DataCentersClient;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.core.exceptions.ClcException;
import com.centurylink.cloud.sdk.core.exceptions.ResourceNotFoundException;
import com.centurylink.cloud.sdk.core.datacenters.client.domain.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenter;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.DataCenterRef;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.IdDataCenterRef;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.NameDataCenterRef;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.centurylink.cloud.sdk.core.datacenters.services.domain.filters.DataCenterFilter.whereIdIs;
import static com.centurylink.cloud.sdk.core.datacenters.services.domain.filters.DataCenterFilter.whereNameContains;
import static com.google.common.collect.Iterables.getFirst;

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
            result = findFirst(whereIdIs(
                dataCenterRef.as(IdDataCenterRef.class).getId()
            ));
        } else if (dataCenterRef.is(NameDataCenterRef.class)) {
            result = findFirst(whereNameContains(
                dataCenterRef.as(NameDataCenterRef.class).getName()
            ));
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

    public DataCenterMetadata findFirst(DataCenterFilter criteria) {
        return getFirst(find(criteria), null);
    }

    public List<DataCenterMetadata> find(Predicate<DataCenterMetadata> predicate) {
        return
            serverClient
                .findAllDataCenters().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
