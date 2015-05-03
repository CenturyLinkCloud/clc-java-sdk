package com.centurylink.cloud.sdk.core.services.refs;

import com.centurylink.cloud.sdk.core.CastMixin;
import com.centurylink.cloud.sdk.core.ToStringMixin;
import com.centurylink.cloud.sdk.core.services.filter.Filter;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Class represent reference on existing resource.
 *
 * @author ilya.drabenia
 */
public interface Reference<F extends Filter<F>> extends CastMixin, ToStringMixin {

    /**
     * Method convert reference to filter object
     *
     * @return filter object
     */
    F asFilter();

}
