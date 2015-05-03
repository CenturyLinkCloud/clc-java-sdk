package com.centurylink.cloud.sdk.sample.port.adapter.web;

import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.sample.domain.SdkRegistry;
import com.centurylink.cloud.sdk.sample.port.adapter.web.beans.DataCenterBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author ilya.drabenia
 */
@RestController
@RequestMapping("/datacenter")
public class DataCenterController {

    @Autowired
    SdkRegistry sdkRegistry;

    @RequestMapping(method = GET)
    List<DataCenterBean> findAll() {
        List<DataCenterMetadata> dataCenters = sdkRegistry.findOrCreate("idrabenia", "RenVortEr9")
                .dataCenterService().findAll();

        List<DataCenterBean> dataCenterBeans = new ArrayList<>(dataCenters.size());
        dataCenters.forEach(dataCenter -> dataCenterBeans.add(new DataCenterBean(dataCenter.getId(), dataCenter.getName())));
        return dataCenterBeans;
    }

}
