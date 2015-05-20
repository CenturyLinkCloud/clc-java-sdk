package com.centurylink.cloud.sdk.servers.services.domain.server;

import com.centurylink.cloud.sdk.servers.services.domain.InfrastructureItem;

/**
 * @author Aliaksandr Krasitski
 */
public interface ServerConfig extends InfrastructureItem {
    CreateServerConfig[] getServerConfig();
}
