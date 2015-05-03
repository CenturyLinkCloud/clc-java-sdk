package com.centurylink.cloud.sdk.base.function;

/**
 * @author Ilya Drabenia
 */
@FunctionalInterface
public interface BinaryPredicate<T> {

    boolean test(T firstArg, T lastArg);

}
