package com.centurylink.cloud.sdk.core.services.function;

import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

/**
 * @author Ilya Drabenia
 */
public abstract class Streams {

    public static <S, T> List<T> map(List<S> source, Function<S, T> mapper) {
        checkNotNull(source, "Source list must be not a null");
        checkNotNull(mapper, "Mapper function must be not a null");

        return
            source
                .stream()
                .map(mapper)
                .collect(toList());
    }

    public static <S, T> List<T> map(S[] source, Function<S, T> mapper) {
        checkNotNull(source, "Source array must be not a null");

        return map(asList(source), mapper);
    }

}
