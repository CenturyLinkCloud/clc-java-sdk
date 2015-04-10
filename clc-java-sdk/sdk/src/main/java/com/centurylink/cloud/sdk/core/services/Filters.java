package com.centurylink.cloud.sdk.core.services;

import java.util.Objects;

import static com.google.common.base.Strings.nullToEmpty;

/**
 * @author Ilya Drabenia
 */
public abstract class Filters {

    public static boolean equals(Object firstObject, Object secondObject) {
        return Objects.equals(firstObject, secondObject);
    }

    public static boolean equalsIgnoreCase(String string, String otherString) {
        return nullToEmpty(string).equalsIgnoreCase(otherString);
    }

    public static boolean containsIgnoreCase(String source, String substring) {
        String sourceInUpperCase = nullToEmpty(source).toUpperCase();
        String substringInUpperCase = nullToEmpty(substring).toUpperCase();

        return sourceInUpperCase.contains(substringInUpperCase);
    }

}
