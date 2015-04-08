package com.centurylink.cloud.sdk.servers;

import com.centurylink.cloud.sdk.tests.AbstractSdkTest;
import com.centurylink.cloud.sdk.core.auth.AuthModule;
import com.google.inject.Module;

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
