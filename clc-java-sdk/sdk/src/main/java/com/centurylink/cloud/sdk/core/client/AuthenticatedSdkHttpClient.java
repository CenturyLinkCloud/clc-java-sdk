package com.centurylink.cloud.sdk.core.client;

import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;

import javax.ws.rs.client.WebTarget;

/**
 * @author aliaksandr.krasitski
 */
public class AuthenticatedSdkHttpClient extends SdkHttpClient {

    protected final BearerAuthentication authentication;

    public AuthenticatedSdkHttpClient(BearerAuthentication authFilter, SdkConfiguration config) {
        super(config);
        this.authentication = authFilter;
    }

    @Override
    protected ResteasyClient buildClient() {
        return super.buildClient().register(authentication);
    }

    protected WebTarget client(String target) {
        return
            buildClient()
                .target(CLC_API_URL + target)
                .resolveTemplate("accountAlias", authentication.getAccountAlias());
    }
}
