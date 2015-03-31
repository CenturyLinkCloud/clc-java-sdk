package com.centurylink.cloud.sdk.core.datacenters.services;

import com.centurylink.cloud.sdk.core.AbstractSdkTest;
import com.centurylink.cloud.sdk.core.auth.AuthModule;
import com.centurylink.cloud.sdk.core.datacenters.DataCentersModule;
import com.centurylink.cloud.sdk.core.datacenters.client.domain.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenter;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenters;
import com.google.inject.Inject;
import com.google.inject.Module;
import org.testng.annotations.Test;

import java.util.List;

public class DataCenterServiceTest extends AbstractSdkTest {

    @Inject
    DataCenterService dataCenterService;

    @Override
    protected List<Module> modules() {
        return list(new AuthModule(), new DataCentersModule());
    }

    @Test
    public void testFindDataCenterByIdRef() {
        DataCenterMetadata dataCenter = dataCenterService.findByRef(DataCenter.refById("DE1"));

        assert dataCenter.getId().equalsIgnoreCase("de1");
    }

    @Test
    public void testFindDataCenterByNameRef() {
        DataCenterMetadata dataCenter = dataCenterService.findByRef(DataCenter.refByName("FrankFurt"));

        assert dataCenter.getId().equalsIgnoreCase("DE1");
    }

    @Test
    public void testFindMultipleDataCentersByRef() {
        List<DataCenterMetadata> dataCenters = dataCenterService.findByRef(
            DataCenters.DE_FRANKFURT,
            DataCenters.CA_TORONTO_1
        );

        assert dataCenters.get(0).getId().equalsIgnoreCase(DataCenters.DE_FRANKFURT.getId());
        assert dataCenters.get(1).getId().equalsIgnoreCase(DataCenters.CA_TORONTO_1.getId());
    }

}