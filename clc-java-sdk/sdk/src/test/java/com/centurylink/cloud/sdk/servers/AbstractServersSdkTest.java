package com.centurylink.cloud.sdk.servers;

import com.centurylink.cloud.sdk.core.auth.config.AuthModule;
import com.centurylink.cloud.sdk.core.auth.domain.credentials.StaticCredentialsProvider;
import com.centurylink.cloud.sdk.servers.config.ServersModule;
import com.google.inject.Guice;
import org.junit.Before;

/**
 * @author ilya.drabenia
 */
public class AbstractServersSdkTest {

    @Before
    public void injectDependencies() {
        Guice
            .createInjector(
                new AuthModule(new StaticCredentialsProvider("idrabenia", "RenVortEr9")),
                new ServersModule()
            )
            .injectMembers(this);
    }

}
