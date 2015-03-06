package com.centurylinkcloud.servers;

import com.centurylinkcloud.core.auth.config.AuthModule;
import com.centurylinkcloud.core.auth.domain.credentials.StaticCredentialsProvider;
import com.centurylinkcloud.servers.config.ServersModule;
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
