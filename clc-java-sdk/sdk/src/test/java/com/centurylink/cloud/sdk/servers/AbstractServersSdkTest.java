package com.centurylink.cloud.sdk.servers;

import com.centurylink.cloud.sdk.core.AbstractSdkTest;
import com.centurylink.cloud.sdk.core.auth.AuthModule;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.PropertiesFileCredentialsProvider;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.StaticCredentialsProvider;
import com.google.inject.Guice;
import com.google.inject.Module;
import org.testng.annotations.BeforeTest;

import java.util.List;

/**
 * @author ilya.drabenia
 */
public class AbstractServersSdkTest extends AbstractSdkTest {

    @Override
    protected List<Module> modules() {
        return list(new AuthModule(), new ServersModule());
    }

}
