package com.centurylink.cloud.sdk.core.exceptions;

import com.centurylink.cloud.sdk.core.services.refs.Reference;

/**
 * Exception represent case when provided reference do not implemented yet in system
 *
 * @author ilya.drabenia
 */
public class ReferenceNotSupportedException extends ClcException {

    public ReferenceNotSupportedException(Class<? extends Reference> type) {
        super("Current type [" + type.getSimpleName() + "] of reference is not supported");
    }

}
