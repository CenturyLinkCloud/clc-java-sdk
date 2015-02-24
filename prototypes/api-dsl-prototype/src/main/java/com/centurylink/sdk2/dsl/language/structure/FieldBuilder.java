package com.centurylink.sdk2.dsl.language.structure;

import com.centurylink.sdk2.dsl.language.PrimitiveType;

/**
 * @author ilya.drabenia
 */
public class FieldBuilder {
    private String name;
    private PrimitiveType type;
    private boolean required;
    private String description;

    public FieldBuilder name(String name) {
        this.name = name;
        return this;
    }

    public FieldBuilder type(PrimitiveType type) {
        this.type = type;
        return this;
    }

    public FieldBuilder required() {
        this.required = true;
        return this;
    }

    public FieldBuilder desc(String description) {
        this.description = description;
        return this;
    }
}
