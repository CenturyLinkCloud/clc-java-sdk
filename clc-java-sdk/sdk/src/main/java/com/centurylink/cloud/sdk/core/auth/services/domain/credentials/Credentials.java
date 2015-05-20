package com.centurylink.cloud.sdk.core.auth.services.domain.credentials;

import com.centurylink.cloud.sdk.core.ToStringMixin;

import java.util.Objects;

/**
 * @author ilya.drabenia
 */
public class Credentials implements ToStringMixin {
    private final String username;
    private final String password;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean isEqualTo(Credentials otherCredentials) {
        return otherCredentials != null &&
            Objects.equals(username, otherCredentials.username) &&
            Objects.equals(password, otherCredentials.password);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return this.toReadableString();
    }
}
