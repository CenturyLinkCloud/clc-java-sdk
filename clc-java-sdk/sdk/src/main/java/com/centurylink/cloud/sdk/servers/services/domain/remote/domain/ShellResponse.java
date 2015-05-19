package com.centurylink.cloud.sdk.servers.services.domain.remote.domain;

/**
 * @author Anton Karavayeu
 */
public class ShellResponse {
    private int errorStatus;
    private String trace;

    public ShellResponse(int errorStatus, String message) {
        this.errorStatus = errorStatus;
        this.trace = message;
    }

    @Override
    public String toString() {
        return "ShellResponse{" +
                "errorStatus=" + errorStatus +
                ", trace='" + trace + '\'' +
                '}';
    }

}
