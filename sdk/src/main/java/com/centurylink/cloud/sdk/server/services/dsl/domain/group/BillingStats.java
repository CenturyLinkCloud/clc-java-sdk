/*
 * (c) 2015 CenturyLink. All Rights Reserved.
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

package com.centurylink.cloud.sdk.server.services.dsl.domain.group;

import java.util.ArrayList;
import java.util.List;

public class BillingStats {
    private String date;
    private List<GroupBilling> groups = new ArrayList<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BillingStats date(String date) {
        setDate(date);
        return this;
    }

    public List<GroupBilling> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupBilling> groups) {
        this.groups = groups;
    }

    public BillingStats group (GroupBilling group) {
        this.groups.add(group);
        return this;
    }

    public BillingStats groups (List<GroupBilling> groups) {
        setGroups(groups);
        return this;
    }
}