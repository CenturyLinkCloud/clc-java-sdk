package com.centurylink.cloud.sdk.servers.client.domain.server.template;

/**
 * @author ilya.drabenia
 */
public class CreateTemplateRequest {
    private String description;
    private String password;
    private String visibility;

    public CreateTemplateRequest() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CreateTemplateRequest description(String description) {
        setDescription(description);
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CreateTemplateRequest password(String password) {
        setPassword(password);
        return this;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public CreateTemplateRequest visibility(String visibility) {
        setVisibility(visibility);
        return this;
    }
}
