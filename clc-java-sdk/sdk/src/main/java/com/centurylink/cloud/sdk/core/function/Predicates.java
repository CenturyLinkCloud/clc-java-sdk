package com.centurylink.cloud.sdk.core.function;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.nullToEmpty;

/**
 * @author Ilya Drabenia
 */
public abstract class Predicates {

    public static <T> Predicate<T> notNull() {
        return (T item) -> item != null;
    }

    public static <T> Predicate<T> alwaysTrue() {
        return new ConstPredicate<>(true);
    }

    public static <T> boolean isAlwaysTruePredicate(Predicate<T> predicate) {
        return
            predicate != null &&
            predicate instanceof ConstPredicate &&
            ConstPredicate.cast(predicate).getDefaultValue() == true;
    }

    public static <T> Predicate<T> alwaysFalse() {
        return new ConstPredicate<>(false);
    }

    public static <T> Predicate<T> in(Stream<T> values) {
        return in(values, Objects::equals);
    }

    public static <T> Predicate<T> in(Stream<T> values, BinaryPredicate<T> matcher) {
        checkNotNull(values, "Stream of values must be not a null");
        checkNotNull(matcher, "Matcher must be not a null");

        return
            values
                .filter(notNull())
                .map(curValue -> (Predicate<T>) t -> matcher.test(t, curValue))
                .reduce(Predicates.alwaysFalse(), Predicate::or);
    }

    @SafeVarargs
    public static <T> Predicate<T> in(T... values) {
        return in(Stream.of(values));
    }

    public static <T> Predicate<T> in(List<T> values) {
        checkNotNull(values, "List of values must be not a null");
        return in(values.stream());
    }

    public static <T> Predicate<T> in(List<T> values, BinaryPredicate<T> matcher) {
        checkNotNull(values, "List of values must be not a null");

        return in(values.stream(), matcher);
    }

    public static <T, R> Predicate<T> combine(Function<T, R> func, Predicate<R> predicate) {
        return r -> predicate.test(func.apply(r));
    }

    public static Predicate<String> containsIgnoreCase(String substring) {
        return item -> containsIgnoreCase(item, substring);
    }

    public static boolean containsIgnoreCase(String source, String substring) {
        return upperCase(source).contains(upperCase(substring));
    }

    public static boolean equalsIgnoreCase(String firstString, String secondString) {
        return upperCase(firstString).equals(upperCase(secondString));
    }

    private static String upperCase(String source) {
        return nullToEmpty(source).toUpperCase();
    }


}
