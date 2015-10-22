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

package com.centurylink.cloud.sdk.policy.services.client.domain.autoscale;

import com.centurylink.cloud.sdk.core.client.domain.Link;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.autoscale.AutoscalePolicyRange;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.autoscale.AutoscalePolicyScaleDownWindow;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "id",
        "name",
        "resourceType",
        "thresholdPeriodMinutes",
        "scaleUpIncrement",
        "range",
        "scaleUpThreshold",
        "scaleDownThreshold",
        "scaleDownWindow",
        "links"
})
public class AutoscalePolicyMetadata {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("resourceType")
    private String resourceType;

    @JsonProperty("thresholdPeriodMinutes")
    private Integer thresholdPeriodMinutes;

    @JsonProperty("scaleUpIncrement")
    private Integer scaleUpIncrement;

    @JsonProperty("range")
    private AutoscalePolicyRange range;

    @JsonProperty("scaleUpThreshold")
    private Integer scaleUpThreshold;

    @JsonProperty("scaleDownThreshold")
    private Integer scaleDownThreshold;

    @JsonProperty("scaleDownWindow")
    private AutoscalePolicyScaleDownWindow scaleDownWindow;

    @JsonProperty("links")
    private List<Link> links = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Integer getThresholdPeriodMinutes() {
        return thresholdPeriodMinutes;
    }

    public void setThresholdPeriodMinutes(Integer thresholdPeriodMinutes) {
        this.thresholdPeriodMinutes = thresholdPeriodMinutes;
    }

    public Integer getScaleUpIncrement() {
        return scaleUpIncrement;
    }

    public void setScaleUpIncrement(Integer scaleUpIncrement) {
        this.scaleUpIncrement = scaleUpIncrement;
    }

    public AutoscalePolicyRange getRange() {
        return range;
    }

    public void setRange(AutoscalePolicyRange range) {
        this.range = range;
    }

    public Integer getScaleUpThreshold() {
        return scaleUpThreshold;
    }

    public void setScaleUpThreshold(Integer scaleUpThreshold) {
        this.scaleUpThreshold = scaleUpThreshold;
    }

    public Integer getScaleDownThreshold() {
        return scaleDownThreshold;
    }

    public void setScaleDownThreshold(Integer scaleDownThreshold) {
        this.scaleDownThreshold = scaleDownThreshold;
    }

    public AutoscalePolicyScaleDownWindow getScaleDownWindow() {
        return scaleDownWindow;
    }

    public void setScaleDownWindow(AutoscalePolicyScaleDownWindow scaleDownWindow) {
        this.scaleDownWindow = scaleDownWindow;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
