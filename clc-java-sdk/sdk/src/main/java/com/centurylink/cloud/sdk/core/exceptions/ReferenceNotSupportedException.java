package com.centurylink.cloud.sdk.core.exceptions;

import com.centurylink.cloud.sdk.core.services.refs.Reference;

/**
 * @author ilya.drabenia
 */
public class ReferenceNotSupportedException extends ClcException {

    public ReferenceNotSupportedException(Class<? extends Reference> type) {
        super("Current type [" + type.getSimpleName() + "] of reference is not supported");
    }

}
