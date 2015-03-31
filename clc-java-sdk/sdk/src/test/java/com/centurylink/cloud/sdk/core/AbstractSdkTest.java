package com.centurylink.cloud.sdk.core;

import com.google.inject.Guice;
import com.google.inject.Module;
import org.testng.annotations.BeforeMethod;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * @author ilya.drabenia
 */
public abstract class AbstractSdkTest {

    @BeforeMethod
    public void injectDependencies() {
        Guice
            .createInjector(modules())
            .injectMembers(this);
    }

    protected List<Module> list(Module... modules) {
        return asList(modules);
    }

    protected abstract List<Module> modules();
}
