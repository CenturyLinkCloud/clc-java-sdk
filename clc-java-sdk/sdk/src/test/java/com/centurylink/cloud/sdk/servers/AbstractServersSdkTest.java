package com.centurylink.cloud.sdk.servers;

import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
import com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerCommand;
import com.centurylink.cloud.sdk.servers.services.domain.server.Machine;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerRef;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import com.centurylink.cloud.sdk.tests.AbstractSdkTest;
import com.centurylink.cloud.sdk.core.auth.AuthModule;
import com.google.inject.Module;

import java.util.List;

import static com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenters.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenters.US_CENTRAL_SALT_LAKE_CITY;
import static com.centurylink.cloud.sdk.servers.services.domain.group.DefaultGroups.DEFAULT_GROUP;
import static com.centurylink.cloud.sdk.servers.services.domain.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.os.OsType.CENTOS;
import static com.centurylink.cloud.sdk.servers.services.domain.server.ServerType.STANDARD;

/**
 * @author ilya.drabenia
 */
public class AbstractServersSdkTest extends AbstractSdkTest {

    @Override
    protected List<Module> modules() {
        return list(new AuthModule(), new ServersModule());
    }

}
