package com.centurylink.cloud.sdk.core.services.function;

import com.centurylink.cloud.sdk.core.services.filter.Filters;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

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
        return
            values
                .filter(notNull())
                .map(curValue -> (Predicate<T>) t -> Objects.equals(curValue, t))
                .reduce(Predicates.alwaysFalse(), Predicate::or);
    }

    @SafeVarargs
    public static <T> Predicate<T> in(T... values) {
        return in(Stream.of(values));
    }

    public static <T, R> Predicate<T> combine(Function<T, R> func, Predicate<R> predicate) {
        return r -> predicate.test(func.apply(r));
    }

    public static Predicate<String> containsIgnoreCase(String substring) {
        return item -> containsIgnoreCase(item, substring);
    }

    private static boolean containsIgnoreCase(String source, String substring) {
        return upperCase(source).contains(upperCase(substring));
    }

    private static String upperCase(String source) {
        return nullToEmpty(source).toUpperCase();
    }


}
