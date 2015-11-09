/*
 * (c) 2015 CenturyLink. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.core.client;

import com.centurylink.cloud.sdk.core.exceptions.ClcException;
import org.jboss.resteasy.client.jaxrs.ProxyBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Map;
import java.util.Properties;

public class ClientWebTargetWrapper implements ResteasyWebTarget {

    private ResteasyWebTarget clientWebTarget;
    private static String userAgent;

    static {
        fetchUserAgentFromProperties();
    }

    public ClientWebTargetWrapper(ResteasyWebTarget clientWebTarget) {
        this.clientWebTarget = clientWebTarget;
    }

    @Override
    public ResteasyWebTarget clone() {
        return new ClientWebTargetWrapper(clientWebTarget.clone());
    }

    @Override
    public ResteasyClient getResteasyClient() {
        return clientWebTarget.getResteasyClient();
    }

    @Override
    public <T> T proxy(Class<T> proxyInterface) {
        return clientWebTarget.proxy(proxyInterface);
    }

    @Override
    public <T> ProxyBuilder<T> proxyBuilder(Class<T> proxyInterface) {
        return clientWebTarget.proxyBuilder(proxyInterface);
    }

    @Override
    public URI getUri() {
        return clientWebTarget.getUri();
    }

    @Override
    public UriBuilder getUriBuilder() {
        return clientWebTarget.getUriBuilder();
    }

    @Override
    public Configuration getConfiguration() {
        return clientWebTarget.getConfiguration();
    }

    @Override
    public ResteasyWebTarget path(String path) throws NullPointerException {
        return clientWebTarget.path(path);
    }

    @Override
    public ResteasyWebTarget path(Class<?> resource) throws IllegalArgumentException {
        return clientWebTarget.path(resource);
    }

    @Override
    public ResteasyWebTarget path(Method method) throws IllegalArgumentException {
        return clientWebTarget.path(method);
    }

    @Override
    public ResteasyWebTarget resolveTemplate(String name, Object value) throws NullPointerException {
        return new ClientWebTargetWrapper(
            clientWebTarget.resolveTemplate(name, value)
        );
    }

    @Override
    public ResteasyWebTarget resolveTemplates(Map<String, Object> templateValues) throws NullPointerException {
        return new ClientWebTargetWrapper(
            clientWebTarget.resolveTemplates(templateValues)
        );
    }

    @Override
    public ResteasyWebTarget resolveTemplate(String name, Object value, boolean encodeSlashInPath)
            throws NullPointerException {
        return new ClientWebTargetWrapper(
            clientWebTarget.resolveTemplate(name, value, encodeSlashInPath)
        );
    }

    @Override
    public ResteasyWebTarget resolveTemplateFromEncoded(String name, Object value) throws NullPointerException {
        return new ClientWebTargetWrapper(
            clientWebTarget.resolveTemplateFromEncoded(name, value)
        );
    }

    @Override
    public ResteasyWebTarget resolveTemplatesFromEncoded(Map<String, Object> templateValues)
            throws NullPointerException {
        return new ClientWebTargetWrapper(
            clientWebTarget.resolveTemplatesFromEncoded(templateValues)
        );
    }

    @Override
    public ResteasyWebTarget resolveTemplates(Map<String, Object> templateValues, boolean encodeSlashInPath)
            throws NullPointerException {
        return new ClientWebTargetWrapper(
            clientWebTarget.resolveTemplates(templateValues, encodeSlashInPath)
        );
    }

    @Override
    public ResteasyWebTarget matrixParam(String name, Object... values) throws NullPointerException {
        return clientWebTarget.matrixParam(name, values);
    }

    @Override
    public ResteasyWebTarget queryParam(String name, Object... values) throws NullPointerException {
        return clientWebTarget.queryParam(name, values);
    }

    @Override
    public ResteasyWebTarget queryParams(MultivaluedMap<String, Object> parameters)
            throws IllegalArgumentException, NullPointerException {
        return clientWebTarget.queryParams(parameters);
    }

    @Override
    public ResteasyWebTarget queryParamNoTemplate(String name, Object... values) throws NullPointerException {
        return clientWebTarget.queryParamNoTemplate(name, values);
    }

    @Override
    public ResteasyWebTarget queryParamsNoTemplate(MultivaluedMap<String, Object> parameters)
            throws IllegalArgumentException, NullPointerException {
        return clientWebTarget.queryParamsNoTemplate(parameters);
    }

    @Override
    public Invocation.Builder request() {
        Invocation.Builder request = clientWebTarget.request();
        setCustomUserAgentHeader(request);

        return request;
    }

    @Override
    public Invocation.Builder request(String... acceptedResponseTypes) {
        Invocation.Builder request = clientWebTarget.request(acceptedResponseTypes);
        setCustomUserAgentHeader(request);

        return request;
    }

    @Override
    public Invocation.Builder request(MediaType... acceptedResponseTypes) {
        Invocation.Builder request = clientWebTarget.request(acceptedResponseTypes);
        setCustomUserAgentHeader(request);

        return request;
    }

    @Override
    public ResteasyWebTarget property(String name, Object value) {
        return clientWebTarget.property(name, value);
    }

    @Override
    public ResteasyWebTarget register(Class<?> componentClass) {
        return clientWebTarget.register(componentClass);
    }

    @Override
    public ResteasyWebTarget register(Class<?> componentClass, int priority) {
        return clientWebTarget.register(componentClass, priority);
    }

    @Override
    public ResteasyWebTarget register(Class<?> componentClass, Class<?>... contracts) {
        return clientWebTarget.register(componentClass, contracts);
    }

    @Override
    public ResteasyWebTarget register(Class<?> componentClass, Map<Class<?>, Integer> contracts) {
        return clientWebTarget.register(componentClass, contracts);
    }

    @Override
    public ResteasyWebTarget register(Object component) {
        return clientWebTarget.register(component);
    }

    @Override
    public ResteasyWebTarget register(Object component, int priority) {
        return clientWebTarget.register(component, priority);
    }

    @Override
    public ResteasyWebTarget register(Object component, Class<?>... contracts) {
        return clientWebTarget.register(component, contracts);
    }

    @Override
    public ResteasyWebTarget register(Object component, Map<Class<?>, Integer> contracts) {
        return clientWebTarget.register(component, contracts);
    }

    private void setCustomUserAgentHeader(Invocation.Builder request) {
        request.header("User-Agent", userAgent);
    }

    private static void fetchUserAgentFromProperties() {
        Properties properties = new Properties();

        try (
            InputStream stream = ClientWebTargetWrapper.class
                .getClassLoader()
                .getResourceAsStream("config.properties")
        ) {
            properties.load(stream);
            userAgent = properties.getProperty("artifactId") + "-v" + properties.getProperty("version");
        } catch (IOException ex) {
            throw new ClcException("Can't load user agent data from config.properties");
        }
    }
}
