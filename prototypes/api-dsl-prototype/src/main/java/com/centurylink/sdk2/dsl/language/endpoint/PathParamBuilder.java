package com.centurylink.sdk2.dsl.language.endpoint;

import com.centurylink.sdk2.dsl.language.PrimitiveType;

/**
 * @author ilya.drabenia
 */
public class PathParamBuilder {
    private String description;
    private String name;
    private PrimitiveType type;
    private boolean required;

    public static PathParamBuilder pathParam() {
        return new PathParamBuilder();
    }

    public PathParamBuilder desc(String description) {
        this.description = description;
        return this;
    }

    public PathParamBuilder name(String name) {
        this.name = name;
        return this;
    }

    public PathParamBuilder type(PrimitiveType type) {
        this.type = type;
        return this;
    }

    public PathParamBuilder required(boolean isRequired) {
        this.required = isRequired;
        return this;
    }
}
