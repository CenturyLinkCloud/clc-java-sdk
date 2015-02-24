package com.centurylink.sdk2.dsl.generator.java;

import com.centurylink.sdk2.dsl.language.PrimitiveType;

/**
 * @author ilya.drabenia
 */
public class Types {

    public Class<?> classOf(PrimitiveType type) {
        if (type == PrimitiveType.STRING) {
            return String.class;
        } else if (type == PrimitiveType.INT) {
            return Integer.class;
        } else if (type == PrimitiveType.BOOLEAN) {
            return Boolean.class;
        } else {
            throw new IllegalArgumentException();
        }
    }

}
