package com.centurylink.cloud.sdk.core.datacenters.services;

import com.centurylink.cloud.sdk.core.datacenters.client.DataCentersClient;
import com.centurylink.cloud.sdk.core.datacenters.client.domain.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.DataCenterRef;
import com.google.inject.Inject;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.services.refs.References.exceptionIfNotFound;
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

    public DataCenterMetadata findByRef(DataCenterRef dataCenterRef) {
        return exceptionIfNotFound(
            findFirst(dataCenterRef.asFilter())
        );
    }

    public List<DataCenterMetadata> findByRef(DataCenterRef... dataCenterRefs) {
        return
            Stream.of(dataCenterRefs)
                .map(this::findByRef)
                .collect(Collectors.toList());
    }

    public DataCenterMetadata findFirst(DataCenterFilter criteria) {
        return getFirst(find(criteria.getPredicate()), null);
    }

    public List<DataCenterMetadata> find(DataCenterFilter criteria) {
        return find(criteria.getPredicate());
    }

    public List<DataCenterMetadata> find(Predicate<DataCenterMetadata> predicate) {
        return
            serverClient
                .findAllDataCenters().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
