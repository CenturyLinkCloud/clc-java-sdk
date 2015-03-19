package com.centurylink.cloud.sdk.core.datacenters.client.domain;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author ilya.drabenia
 */
public class GetDataCenterListResponse extends ArrayList<GetDataCenterResponse> {

    public GetDataCenterListResponse(int initialCapacity) {
        super(initialCapacity);
    }

    public GetDataCenterListResponse() {
    }

    public GetDataCenterListResponse(Collection<? extends GetDataCenterResponse> c) {
        super(c);
    }

    public GetDataCenterResponse findById(String id) {
        for (GetDataCenterResponse curResponse : this) {
            if (curResponse.getId().equalsIgnoreCase(id)) {
                return curResponse;
            }
        }

        return null;
    }

    public GetDataCenterResponse findWhereNameContains(String name) {
        String matchSubstring = name.toUpperCase();

        for (GetDataCenterResponse curResponse : this) {
            String upperCaseName = curResponse.getName().toUpperCase();
            if (upperCaseName.contains(matchSubstring)) {
                return curResponse;
            }
        }

        return null;
    }
}
