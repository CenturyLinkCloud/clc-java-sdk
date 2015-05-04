package com.centurylink.cloud.sdk.common.management.client.domain.datacenters;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author ilya.drabenia
 */
public class GetDataCenterListResponse extends ArrayList<DataCenterMetadata> {

    public GetDataCenterListResponse(int initialCapacity) {
        super(initialCapacity);
    }

    public GetDataCenterListResponse() {
    }

    public GetDataCenterListResponse(Collection<? extends DataCenterMetadata> c) {
        super(c);
    }

    public DataCenterMetadata findById(String id) {
        for (DataCenterMetadata curResponse : this) {
            if (curResponse.getId().equalsIgnoreCase(id)) {
                return curResponse;
            }
        }

        return null;
    }

    public DataCenterMetadata findWhereNameContains(String name) {
        String matchSubstring = name.toUpperCase();

        for (DataCenterMetadata curResponse : this) {
            String upperCaseName = curResponse.getName().toUpperCase();
            if (upperCaseName.contains(matchSubstring)) {
                return curResponse;
            }
        }

        return null;
    }
}
