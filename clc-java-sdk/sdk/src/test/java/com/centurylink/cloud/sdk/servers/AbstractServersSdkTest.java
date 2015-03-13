package com.centurylink.cloud.sdk.servers;

import com.centurylink.cloud.sdk.core.auth.AuthModule;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.StaticCredentialsProvider;
import com.google.inject.Guice;
import org.testng.annotations.BeforeTest;

/**
 * @author ilya.drabenia
 */
public class AbstractServersSdkTest {

    @BeforeTest
    public void injectDependencies() {
        Guice
            .createInjector(
                new AuthModule(new StaticCredentialsProvider("idrabenia", "RenVortEr9")),
                new ServersModule()
            )
            .injectMembers(this);
    }

}
