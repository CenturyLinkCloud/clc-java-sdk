package com.centurylink.cloud.sdk.tests.fixtures;

import com.centurylink.cloud.sdk.ClcSdk;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
import com.centurylink.cloud.sdk.servers.services.domain.server.*;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerRef;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.time.ZonedDateTime;

import static com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenters.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.servers.services.domain.group.DefaultGroups.DEFAULT_GROUP;
import static com.centurylink.cloud.sdk.servers.services.domain.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.os.OsType.CENTOS;
import static com.centurylink.cloud.sdk.servers.services.domain.server.ServerType.STANDARD;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;

/**
 * @author Ilya Drabenia
 */
public class SingleServerFixture {

    private static volatile ServerRef server;

    public static ServerRef server() {
        return server;
    }

    @BeforeSuite(groups = LONG_RUNNING)
    public void createServer() {
        server =
            new ClcSdk()
                .serverService()
                .create(new CreateServerCommand()
                    .name("TCRT")
                    .type(STANDARD)
                    .group(Group.refByName()
                        .name(DEFAULT_GROUP)
                        .dataCenter(DataCenter.refByName("FranKfUrt"))
                    )
                    .timeToLive(ZonedDateTime.now().plusDays(1))
                    .machine(new Machine()
                        .cpuCount(1)
                        .ram(3)
                        .disk(new DiskConfig()
                            .type(DiskType.RAW)
                            .size(14)
                        )
                    )
                    .template(Template.refByOs()
                        .dataCenter(DE_FRANKFURT)
                        .type(CENTOS)
                        .version("6")
                        .architecture(x86_64)
                    )
                    .network(new NetworkConfig()
                        .primaryDns("172.17.1.26")
                        .secondaryDns("172.17.1.27")
                    )
                )
                .waitUntilComplete()
                .getResult()
                .asRefById();
    }

    @AfterSuite(groups = LONG_RUNNING)
    public void deleteServer() {
        new ClcSdk()
            .serverService()
            .delete(server)
            .waitUntilComplete();
    }

}
