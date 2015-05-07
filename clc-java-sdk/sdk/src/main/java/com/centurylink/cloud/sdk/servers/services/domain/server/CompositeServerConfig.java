package com.centurylink.cloud.sdk.servers.services.domain.server;

/**
 * Represents multiple identical server configs.<br/>
 * Store {@code count} number of copies of {@code server}
 *
 * @author Aliaksandr Krasitski
 */
public class CompositeServerConfig implements ServerConfig {
    private int count = 1;
    private CreateServerConfig server;

    public CompositeServerConfig count(int count) {
        assert count > 0;
        this.count = count;
        return this;
    }

    public int getCount() {
        return count;
    }

    public CompositeServerConfig server(CreateServerConfig server) {
        this.server = server;
        return this;
    }

    public CreateServerConfig getServer() {
        return server;
    }

    @Override
    public CreateServerConfig[] getServerConfig() {
        CreateServerConfig[] copies = new CreateServerConfig[count];
        for (int i=0; i<count;i++) {
            copies[i] = (server);
        }
        return copies;
    }
}
