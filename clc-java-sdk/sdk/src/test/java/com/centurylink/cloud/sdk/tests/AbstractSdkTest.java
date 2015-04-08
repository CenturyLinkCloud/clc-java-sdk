package com.centurylink.cloud.sdk.tests;

import com.centurylink.cloud.sdk.tests.mocks.BindInjectedMocksModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * @author ilya.drabenia
 */
public abstract class AbstractSdkTest {

    @BeforeMethod(alwaysRun = true)
    public void injectDependencies() {
        MockitoAnnotations.initMocks(this);
        Guice
            .createInjector(composeModules())
            .injectMembers(this);
    }

    private Module composeModules() {
        return Modules.override(modules()).with(new BindInjectedMocksModule(this));
    }

    protected List<Module> list(Module... modules) {
        return asList(modules);
    }

    protected abstract List<Module> modules();
}
