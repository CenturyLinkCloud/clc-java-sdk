package com.centurylink.cloud.sdk.servers.services.domain.server;

import com.centurylink.cloud.sdk.servers.services.domain.server.refs.IdServerRef;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerRef;

/**
 * @author ilya.drabenia
 */
public class Server {
    private String id;
    private String name;
    private String description;
    private String status;

    public Server() {
    }

    public Server(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static ServerRef refById(String id) {
        return new IdServerRef(id);
    }
}
