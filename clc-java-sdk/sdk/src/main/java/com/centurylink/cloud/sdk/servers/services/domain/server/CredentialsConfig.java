package com.centurylink.cloud.sdk.servers.services.domain.server;

public class CredentialsConfig {

    private String oldPassword;
    private String newPassword;

    public CredentialsConfig() {}

    public CredentialsConfig(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public CredentialsConfig oldPassword(String oldPassword) {
        setOldPassword(oldPassword);
        return this;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public CredentialsConfig newPassword(String newPassword) {
        setNewPassword(newPassword);
        return this;
    }
}
