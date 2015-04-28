package com.centurylink.cloud.sdk.servers.services.domain.template;

import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;

/**
 * @author ilya.drabenia
 */
public class CreateTemplateCommand {
    public enum Visibility { PRIVATE, PRIVATE_SHARED }

    private Server server;
    private String description;
    private String password;
    private Visibility visibility;

    public CreateTemplateCommand() {
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public CreateTemplateCommand server(Server server) {
        setServer(server);
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CreateTemplateCommand description(String description) {
        setDescription(description);
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CreateTemplateCommand password(String password) {
        setPassword(password);
        return this;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public CreateTemplateCommand visibility(Visibility visibility) {
        setVisibility(visibility);
        return this;
    }
}
