package com.centurylink.cloud.sdk.core.auth.services;

import com.google.inject.Inject;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @author aliaksandr.krasitski
 */
public class ProxyAuthentication implements ClientRequestFilter {
    private final String user;
    private final String password;

    @Inject
    public ProxyAuthentication(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public void filter(ClientRequestContext requestContext) throws IOException {
        requestContext.getHeaders().add("Proxy-Authorization", getBasicAuthentication());
    }

    private String getBasicAuthentication() {
        String token = this.user + ":" + this.password;
        try {
            return "Basic " + Base64.getEncoder().encodeToString(token.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("Cannot encode with UTF-8", ex);
        }
    }
}
