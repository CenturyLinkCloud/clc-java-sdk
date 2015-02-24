package com.centurylink.sdk2.dsl.language.structure;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ilya.drabenia
 */
public class StructureBuilder {
    private String name;
    private List<FieldBuilder> fields = new ArrayList<>();

    public StructureBuilder() {
    }

    public static StructureBuilder structure() {
        return new StructureBuilder();
    }

    public StructureBuilder name(String name) {
        this.name = name;
        return this;
    }

    public StructureBuilder field(FieldBuilder field) {
        fields.add(field);
        return this;
    }

}
