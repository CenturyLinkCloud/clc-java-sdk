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

package com.centurylink.cloud.sdk.servers;

import com.centurylink.cloud.sdk.servers.services.domain.ip.CreatePublicIpConfig;
import com.centurylink.cloud.sdk.servers.services.domain.ip.port.PortConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.*;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.Template;

import java.time.ZonedDateTime;

import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.US_CENTRAL_SALT_LAKE_CITY;
import static com.centurylink.cloud.sdk.servers.services.domain.server.ServerType.STANDARD;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsType.CENTOS;

/**
 * @author Ilya Drabenia
 */
public class SampleServerConfigs {

    public static CreateServerConfig centOsServer(String name) {
        return new CreateServerConfig()
            .name(name)
            .description(name)
            .type(STANDARD)
            .machine(new Machine()
                    .cpuCount(1)
                    .ram(2)
            )
            .template(Template.refByOs()
                    .dataCenter(US_CENTRAL_SALT_LAKE_CITY)
                    .type(CENTOS)
                    .version("6")
                    .architecture(x86_64)
            )
            .timeToLive(
                ZonedDateTime.now().plusHours(2)
            );
    }

    public static CreateServerConfig mysqlServer() {
        CreateServerConfig mySqlSrv = centOsServer("MySQL");

        mySqlSrv.getMachine()
            .disk(new DiskConfig()
                .type(DiskType.RAW)
                .size(10));

        return mySqlSrv;
    }

    public static CreateServerConfig nginxServer() {

        return centOsServer("Nginx")
            .network(new NetworkConfig()
                .publicIpConfig(new CreatePublicIpConfig()
                    .openPorts(PortConfig.HTTP)));
    }

    public static CreateServerConfig apacheHttpServer() {
        return centOsServer("Apache");
    }

}
