package com.centurylink.cloud.sdk.core.services.filter;

import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.google.common.base.Strings.nullToEmpty;
import static java.util.Arrays.copyOfRange;

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

    @SafeVarargs
    static <T extends Filter<T>> T reduce(BinaryOperator<T> operator, T... filters) {
        int length = filters.length;
        T head = filters[0];
        T[] tail = copyOfRange(filters, 1, length);

        if (length == 1) {
            return head;
        } else if (length == 2) {
            return operator.apply(head, tail[0]);
        } else {
            return operator.apply(head, reduce(operator, tail));
        }
    }

}
