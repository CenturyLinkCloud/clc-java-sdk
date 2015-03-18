package com.centurylink.cloud.sdk.core.exceptions;

import com.centurylink.cloud.sdk.core.domain.BaseRef;

/**
 * @author ilya.drabenia
 */
public class ReferenceNotSupportedException extends ClcException {

    public ReferenceNotSupportedException(Class<? extends BaseRef> type) {
        super("Current type [" + type.getSimpleName() + "] of reference is not supported");
    }

}
