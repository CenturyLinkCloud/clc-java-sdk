package com.centurylink.cloud.sdk.core.services.filter;

import org.apache.http.HttpStatus;

import javax.ws.rs.ClientErrorException;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * @author Ilya Drabenia
 */
public abstract class Filters {

    //Forbidden added because of access denied for deleted groups
    private static List<Integer> processedCodes = Arrays.asList(new Integer[] {
        HttpStatus.SC_NOT_FOUND,
        HttpStatus.SC_FORBIDDEN}
    );

    static <T extends Filter<T>> T reduce(List<T> filters, BinaryOperator<T> operator) {
        int length = filters.size();
        T head = filters.get(0);
        List<T> tail = filters.subList(1, length);

        if (length == 1) {
            return head;
        } else if (length == 2) {
            return operator.apply(head, tail.get(0));
        } else {
            return operator.apply(head, reduce(tail, operator));
        }
    }

    public static <T, R> Function<T, R> nullable(Function<T, R> supplier) {
        return (T val) -> {
            try {
                return supplier.apply(val);
            } catch (ClientErrorException ex) {
                if (processedCodes.contains(ex.getResponse().getStatus())) {
                    return null;
                }
                throw new ClientErrorException(ex.getResponse());
            }
        };
    }

}
