/*
 * (c) 2015 CenturyLink. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.sample.port.adapter.web;

import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.sample.domain.SdkCredentials;
import com.centurylink.cloud.sdk.sample.domain.SdkRegistry;
import com.centurylink.cloud.sdk.sample.port.adapter.web.beans.DataCenterBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
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
        List<DataCenterMetadata> dataCenters =
            sdkRegistry
                .getSdk()
                .dataCenterService()
                .findAll();

        List<DataCenterBean> dataCenterBeans = new ArrayList<>(dataCenters.size());
        dataCenters.forEach(dataCenter -> dataCenterBeans.add(new DataCenterBean(dataCenter.getId(), dataCenter.getName())));
        return dataCenterBeans;
    }

}
