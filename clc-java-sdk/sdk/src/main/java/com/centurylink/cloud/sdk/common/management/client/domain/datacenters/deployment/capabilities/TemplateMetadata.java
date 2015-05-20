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

package com.centurylink.cloud.sdk.common.management.client.domain.datacenters.deployment.capabilities;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "name",
        "osType",
        "description",
        "storageSizeGB",
        "capabilities",
        "reservedDrivePaths",
        "drivePathLength"
})
public class TemplateMetadata {

    @JsonProperty("name")
    private String name;
    @JsonProperty("osType")
    private String osType;
    @JsonProperty("description")
    private String description;
    @JsonProperty("storageSizeGB")
    private Integer storageSizeGB;
    @JsonProperty("capabilities")
    private List<String> capabilities = new ArrayList<String>();
    @JsonProperty("reservedDrivePaths")
    private List<String> reservedDrivePaths = new ArrayList<String>();
    @JsonProperty("drivePathLength")
    private Integer drivePathLength;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    @JsonIgnore
    public static final String MANAGED_OS_VALUE = "managedOS";

    /**
     *
     * @return
     * The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The osType
     */
    @JsonProperty("osType")
    public String getOsType() {
        return osType;
    }

    /**
     *
     * @param osType
     * The osType
     */
    @JsonProperty("osType")
    public void setOsType(String osType) {
        this.osType = osType;
    }

    /**
     *
     * @return
     * The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The storageSizeGB
     */
    @JsonProperty("storageSizeGB")
    public Integer getStorageSizeGB() {
        return storageSizeGB;
    }

    /**
     *
     * @param storageSizeGB
     * The storageSizeGB
     */
    @JsonProperty("storageSizeGB")
    public void setStorageSizeGB(Integer storageSizeGB) {
        this.storageSizeGB = storageSizeGB;
    }

    /**
     *
     * @return
     * The capabilities
     */
    @JsonProperty("capabilities")
    public List<String> getCapabilities() {
        return capabilities;
    }

    /**
     *
     * @param capabilities
     * The capabilities
     */
    @JsonProperty("capabilities")
    public void setCapabilities(List<String> capabilities) {
        this.capabilities = capabilities;
    }

    /**
     *
     * @return
     * The reservedDrivePaths
     */
    @JsonProperty("reservedDrivePaths")
    public List<String> getReservedDrivePaths() {
        return reservedDrivePaths;
    }

    /**
     *
     * @param reservedDrivePaths
     * The reservedDrivePaths
     */
    @JsonProperty("reservedDrivePaths")
    public void setReservedDrivePaths(List<String> reservedDrivePaths) {
        this.reservedDrivePaths = reservedDrivePaths;
    }

    /**
     *
     * @return
     * The drivePathLength
     */
    @JsonProperty("drivePathLength")
    public Integer getDrivePathLength() {
        return drivePathLength;
    }

    /**
     *
     * @param drivePathLength
     * The drivePathLength
     */
    @JsonProperty("drivePathLength")
    public void setDrivePathLength(Integer drivePathLength) {
        this.drivePathLength = drivePathLength;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @JsonIgnore
    public boolean hasCapability(String capability) {
        return this.capabilities == null ? false : this.capabilities.indexOf(capability) > -1;
    }

}