package com.centurylink.cloud.sdk.tests;

import com.centurylink.cloud.sdk.core.services.ClcServiceException;
import com.centurylink.cloud.sdk.servers.TestModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

/**
 * @author ilya.drabenia
 */
public abstract class AbstractSdkTest {
    protected final Logger logger = LoggerFactory.getLogger(getClass());


    @BeforeMethod(alwaysRun = true)
    public void injectDependencies() {
        MockitoAnnotations.initMocks(this);
        Guice
            .createInjector(composeModules())
            .injectMembers(this);
    }

    @SuppressWarnings("unchecked")
    private Module composeModules() {
        return Modules.override(modules()).with(new TestModule() {

            @Override
            protected void configure() {
                Stream
                    .of(declaredFields())
                    .filter(f -> f.isAnnotationPresent(Mock.class))
                    .forEach(f -> bind((Class<Object>) f.getType()).toInstance(fieldValue(f)));

                override(this);
            }

        });
    }

    private Field[] declaredFields() {
        return AbstractSdkTest.this
            .getClass()
            .getDeclaredFields();
    }

    private Object fieldValue(Field f) {
        try {
            f.setAccessible(true);
            return f.get(AbstractSdkTest.this);
        } catch (IllegalAccessException e) {
            throw new ClcServiceException("Could not access field value", e);
        }
    }

    protected List<Module> list(Module... modules) {
        return asList(modules);
    }

    protected void override(TestModule module) {
        // noop
    }

    protected abstract List<Module> modules();
}
