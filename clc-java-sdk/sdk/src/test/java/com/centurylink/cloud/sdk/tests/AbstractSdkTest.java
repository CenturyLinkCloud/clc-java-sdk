package com.centurylink.cloud.sdk.tests;

import com.centurylink.cloud.sdk.core.client.ClcClientException;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.deployment.capabilities.DatacenterDeploymentCapabilitiesMetadata;
import com.centurylink.cloud.sdk.tests.mocks.BindMocksModule;
import com.centurylink.cloud.sdk.tests.mocks.BindSpiesModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

/**
 * @author ilya.drabenia
 */
public abstract class AbstractSdkTest extends Assert {

    @BeforeMethod(alwaysRun = true)
    public void injectDependencies() {
        Guice
            .createInjector(composeModules())
            .injectMembers(this);
    }

    private Module composeModules() {
        return Modules.override(modules()).with(
            new BindMocksModule(this),
            new BindSpiesModule(this)
        );
    }

    protected List<Module> list(Module... modules) {
        return asList(modules);
    }

    protected abstract List<Module> modules();


    public <T> T fromJson(String filePath, Class<T> type) {
        try {
            return
                new ObjectMapper().readValue(
                    getClass().getResourceAsStream(filePath), type
                );
        } catch (IOException e) {
            throw new ClcClientException(e);
        }
    }

}
