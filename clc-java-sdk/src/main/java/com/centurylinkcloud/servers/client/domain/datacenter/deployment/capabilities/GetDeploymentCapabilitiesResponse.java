package com.centurylinkcloud.servers.client.domain.datacenter.deployment.capabilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

import com.centurylinkcloud.servers.domain.os.OperatingSystem;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "dataCenterEnabled",
        "importVMEnabled",
        "supportsPremiumStorage",
        "supportsSharedLoadBalancer",
        "deployableNetworks",
        "templates",
        "importableOSTypes"
})
public class GetDeploymentCapabilitiesResponse {

    @JsonProperty("dataCenterEnabled")
    private Boolean dataCenterEnabled;
    @JsonProperty("importVMEnabled")
    private Boolean importVMEnabled;
    @JsonProperty("supportsPremiumStorage")
    private Boolean supportsPremiumStorage;
    @JsonProperty("supportsSharedLoadBalancer")
    private Boolean supportsSharedLoadBalancer;
    @JsonProperty("deployableNetworks")
    private List<DeployableNetwork> deployableNetworks = new ArrayList<DeployableNetwork>();
    @JsonProperty("templates")
    private List<Template> templates = new ArrayList<Template>();
    @JsonProperty("importableOSTypes")
    private List<ImportableOSType> importableOSTypes = new ArrayList<ImportableOSType>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The dataCenterEnabled
     */
    @JsonProperty("dataCenterEnabled")
    public Boolean getDataCenterEnabled() {
        return dataCenterEnabled;
    }

    /**
     *
     * @param dataCenterEnabled
     * The dataCenterEnabled
     */
    @JsonProperty("dataCenterEnabled")
    public void setDataCenterEnabled(Boolean dataCenterEnabled) {
        this.dataCenterEnabled = dataCenterEnabled;
    }

    /**
     *
     * @return
     * The importVMEnabled
     */
    @JsonProperty("importVMEnabled")
    public Boolean getImportVMEnabled() {
        return importVMEnabled;
    }

    /**
     *
     * @param importVMEnabled
     * The importVMEnabled
     */
    @JsonProperty("importVMEnabled")
    public void setImportVMEnabled(Boolean importVMEnabled) {
        this.importVMEnabled = importVMEnabled;
    }

    /**
     *
     * @return
     * The supportsPremiumStorage
     */
    @JsonProperty("supportsPremiumStorage")
    public Boolean getSupportsPremiumStorage() {
        return supportsPremiumStorage;
    }

    /**
     *
     * @param supportsPremiumStorage
     * The supportsPremiumStorage
     */
    @JsonProperty("supportsPremiumStorage")
    public void setSupportsPremiumStorage(Boolean supportsPremiumStorage) {
        this.supportsPremiumStorage = supportsPremiumStorage;
    }

    /**
     *
     * @return
     * The supportsSharedLoadBalancer
     */
    @JsonProperty("supportsSharedLoadBalancer")
    public Boolean getSupportsSharedLoadBalancer() {
        return supportsSharedLoadBalancer;
    }

    /**
     *
     * @param supportsSharedLoadBalancer
     * The supportsSharedLoadBalancer
     */
    @JsonProperty("supportsSharedLoadBalancer")
    public void setSupportsSharedLoadBalancer(Boolean supportsSharedLoadBalancer) {
        this.supportsSharedLoadBalancer = supportsSharedLoadBalancer;
    }

    /**
     *
     * @return
     * The deployableNetworks
     */
    @JsonProperty("deployableNetworks")
    public List<DeployableNetwork> getDeployableNetworks() {
        return deployableNetworks;
    }

    /**
     *
     * @param deployableNetworks
     * The deployableNetworks
     */
    @JsonProperty("deployableNetworks")
    public void setDeployableNetworks(List<DeployableNetwork> deployableNetworks) {
        this.deployableNetworks = deployableNetworks;
    }

    /**
     *
     * @return
     * The templates
     */
    @JsonProperty("templates")
    public List<Template> getTemplates() {
        return templates;
    }

    /**
     *
     * @param templates
     * The templates
     */
    @JsonProperty("templates")
    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    /**
     *
     * @return
     * The importableOSTypes
     */
    @JsonProperty("importableOSTypes")
    public List<ImportableOSType> getImportableOSTypes() {
        return importableOSTypes;
    }

    /**
     *
     * @param importableOSTypes
     * The importableOSTypes
     */
    @JsonProperty("importableOSTypes")
    public void setImportableOSTypes(List<ImportableOSType> importableOSTypes) {
        this.importableOSTypes = importableOSTypes;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Template findByOsType(OperatingSystem operatingSystem) {
        for (Template curTemplate : this.getTemplates()) {
            if (templateHasOs(curTemplate, operatingSystem)) {
                return curTemplate;
            }
        }

        return null;
    }

    private boolean templateHasOs(Template curTemplate, OperatingSystem operatingSystem) {
        return osTypeContains(
            curTemplate,
            operatingSystem.getType(),
            operatingSystem.getVersion(),
            operatingSystem.getEdition(),
            operatingSystem.getArchitecture().getCode()
        );
    }

    private boolean osTypeContains(Template curTemplate, String... values) {
        for (String curValue : values) {
            if (curValue != null && !curTemplate.getOsType().contains(curValue)) {
                return false;
            }
        }

        return true;
    }

}