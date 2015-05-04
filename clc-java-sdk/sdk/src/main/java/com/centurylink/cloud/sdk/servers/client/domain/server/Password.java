package com.centurylink.cloud.sdk.servers.client.domain.server;

public class Password {

    private String current;
    private String password;

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public Password current(String current) {
        setCurrent(current);
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Password password(String password) {
        setCurrent(current);
        return this;
    }
}
