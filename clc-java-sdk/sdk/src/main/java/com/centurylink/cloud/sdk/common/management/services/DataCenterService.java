package com.centurylink.cloud.sdk.common.management.services;

import com.centurylink.cloud.sdk.common.management.client.DataCentersClient;
import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.google.inject.Inject;

import java.util.List;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.services.refs.Reference.evalUsingFindByFilter;
import static java.util.stream.Collectors.toList;

/**
 * @author ilya.drabenia
 */
public class DataCenterService {
    private final DataCentersClient serverClient;

    @Inject
    public DataCenterService(DataCentersClient serverClient) {
        this.serverClient = serverClient;
    }

    public DataCenterMetadata findByRef(DataCenter dataCenterRef) {
        return evalUsingFindByFilter(dataCenterRef, this::find);
    }

    public List<DataCenterMetadata> find(DataCenterFilter criteria) {
        return findLazy(criteria).collect(toList());
    }

    public Stream<DataCenterMetadata> findLazy(DataCenterFilter criteria) {
        return findAll().stream().filter(criteria.getPredicate());
    }

    public List<DataCenterMetadata> findAll() {
        return serverClient.findAllDataCenters();
    }
}
