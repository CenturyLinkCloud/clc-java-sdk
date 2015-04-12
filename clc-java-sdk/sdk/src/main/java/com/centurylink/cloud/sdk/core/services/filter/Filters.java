package com.centurylink.cloud.sdk.core.services.filter;

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
        return upperCase(source).contains(upperCase(substring));
    }

    private static String upperCase(String source) {
        return nullToEmpty(source).toUpperCase();
    }

}
