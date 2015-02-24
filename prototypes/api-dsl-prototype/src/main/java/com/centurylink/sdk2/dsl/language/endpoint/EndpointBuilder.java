package com.centurylink.sdk2.dsl.language.endpoint;

import com.centurylink.sdk2.dsl.language.structure.StructureSpecification;
import com.google.common.base.Joiner;

/**
 * @author ilya.drabenia
 */
public class EndpointBuilder {
    private String endpointName;
    private String description;
    private String url;
    private Method method;
    private StructureSpecification request;
    private StructureSpecification response;

    public EndpointBuilder(String endpointName) {
        this.endpointName = endpointName;
    }

    public static EndpointBuilder endpoint(String name) {
        return new EndpointBuilder(name);
    }

    public EndpointBuilder desc(String description) {
        this.description = description;
        return this;
    }

    public EndpointBuilder desc(String... descriptionLines) {
        return desc(Joiner.on("\n").join(descriptionLines));
    }

    public EndpointBuilder url(String url) {
        this.url = url;
        return this;
    }

    public EndpointBuilder method(Method method) {
        this.method = method;
        return this;
    }

    public EndpointBuilder param(Object... args) {
        return this;
    }

    public EndpointBuilder request(StructureSpecification request) {
        this.request = request;
        return this;
    }

    public EndpointBuilder response(StructureSpecification response) {
        this.response = response;
        return this;
    }

    public EndpointBuilder end() {
        return this;
    }

}
