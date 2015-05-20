/*
 * (c) 2015 CenturyLink Cloud. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.core.exceptions;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions.notNull;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.codec.CharEncoding.UTF_8;

/**
 * @author Ilya Drabenia
 */
public class ErrorsContainer {
    private final List<Exception> errors = new CopyOnWriteArrayList<>();
    private final Function<String, ClcException> exceptionSupplier;

    public ErrorsContainer(Function<String, ClcException> exceptionSupplier) {
        notNull(exceptionSupplier, "Exception supplier must be not null");

        this.exceptionSupplier = exceptionSupplier;
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }

    private String getStackTrace(Exception ex) {
        try {
            ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
            PrintStream out = new PrintStream(outBytes, true, UTF_8);

            ex.printStackTrace(out);

            return outBytes.toString(UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new ClcException(e);
        }
    }

    public ClcException summaryException() {
        if (hasErrors()) {
            ClcException ex = exceptionSupplier.apply(errorMessage());
            errors.forEach(ex::addSubException);
            return ex;
        } else {
            return null;
        }
    }

    private String errorMessage() {
        return "\n" +
            errors
                .stream()
                .map(this::getStackTrace)
                .collect(joining("\n"));
    }

    public void throwSummaryExceptionIfNeeded() {
        if (hasErrors()) {
            throw summaryException();
        }
    }

    public <T> Consumer<T> intercept(Consumer<T> func) {
        return (T val) -> {
            try {
                func.accept(val);
            } catch (ClcException ex) {
                errors.add(ex);
            }
        };
    }

    public ErrorsContainer add(Exception ex) {
        errors.add(ex);
        return this;
    }

    public ErrorsContainer addAll(Collection<Exception> exceptions) {
        errors.addAll(exceptions);
        return this;
    }

}
