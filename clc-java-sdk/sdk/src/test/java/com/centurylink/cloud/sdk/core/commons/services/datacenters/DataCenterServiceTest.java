package com.centurylink.cloud.sdk.core.commons.services.datacenters;

import com.centurylink.cloud.sdk.core.commons.client.DataCentersClient;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.GetDataCenterListResponse;
import com.centurylink.cloud.sdk.core.commons.services.DataCenterService;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.tests.AbstractSdkTest;
import com.centurylink.cloud.sdk.core.auth.AuthModule;
import com.centurylink.cloud.sdk.core.commons.CommonsModule;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenterByIdRef;
import com.google.inject.Inject;
import com.google.inject.Module;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter.CA_TORONTO_1;
import static com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;

@Test(groups = INTEGRATION)
public class DataCenterServiceTest extends AbstractSdkTest {

    @Inject
    DataCenterService dataCenterService;

    @Inject @Spy
    DataCentersClient dataCentersClient;

    @Override
    protected List<Module> modules() {
        return list(new AuthModule(), new CommonsModule());
    }

    @BeforeMethod
    public void setUp() throws Exception {
        Mockito
            .doReturn(fromJson("data_centers_list.json", GetDataCenterListResponse.class))
            .when(dataCentersClient).findAllDataCenters();
    }

    @Test
    public void testFindDataCenterByIdRef() {
        DataCenterMetadata dataCenter = dataCenterService.findByRef(DataCenter.refById("de1"));

        assert dataCenter.getId().equalsIgnoreCase("de1");
    }

    @Test
    public void testFindDataCenterByNameRef() {
        DataCenterMetadata dataCenter = dataCenterService.findByRef(DataCenter.refByName("FrankFurt"));

        assert dataCenter.getId().equalsIgnoreCase("DE1");
    }

    @Test
    public void testFindDataCenterByName() {
        List<DataCenterMetadata> results =
            dataCenterService
                .find(new DataCenterFilter()
                    .nameContains("Canada")
                );

        assertEquals(results.size(), 3);
    }

    @Test
    public void testFindDataCenterByMultipleId() {
        List<DataCenterMetadata> results =
            dataCenterService
                .find(new DataCenterFilter()
                    .id("de1", "va1")
                );

        verifyDataCenterById(results, 0, DE_FRANKFURT);
        verifyDataCenterById(results, 1, DataCenter.CA_VANCOUVER);
    }

    public boolean verifyDataCenterById(List<DataCenterMetadata> dataCenters, int index,
                                        DataCenterByIdRef expectedDataCenter) {
        return dataCenters.get(index).getId().equalsIgnoreCase(expectedDataCenter.getId());
    }

}