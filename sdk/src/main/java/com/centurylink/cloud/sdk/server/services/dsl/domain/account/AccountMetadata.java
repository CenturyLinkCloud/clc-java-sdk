package com.centurylink.cloud.sdk.server.services.dsl.domain.account;

/**
 * Created by aliaksandr.krasitski on 5/19/2015.
 */
public class AccountMetadata {
    private String name;

    public AccountMetadata(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
