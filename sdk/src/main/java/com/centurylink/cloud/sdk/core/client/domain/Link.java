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

package com.centurylink.cloud.sdk.core.client.domain;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "rel",
        "href",
        "verbs",
        "id",
        "name"
})
public class Link {

    @JsonProperty("rel")
    private String rel;

    @JsonProperty("href")
    private String href;

    @JsonProperty("verbs")
    private List<String> verbs = new ArrayList<String>();

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The rel
     */
    @JsonProperty("rel")
    public String getRel() {
        return rel;
    }

    /**
     *
     * @param rel
     * The rel
     */
    @JsonProperty("rel")
    public void setRel(String rel) {
        this.rel = rel;
    }

    /**
     *
     * @return
     * The href
     */
    @JsonProperty("href")
    public String getHref() {
        return href;
    }

    /**
     *
     * @param href
     * The href
     */
    @JsonProperty("href")
    public void setHref(String href) {
        this.href = href;
    }

    /**
     *
     * @return
     * The verbs
     */
    @JsonProperty("verbs")
    public List<String> getVerbs() {
        return verbs;
    }

    /**
     *
     * @param verbs
     * The verbs
     */
    @JsonProperty("verbs")
    public void setVerbs(List<String> verbs) {
        this.verbs = verbs;
    }

    /**
     *
     * @return
     * The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
