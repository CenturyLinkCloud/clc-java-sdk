package com.centurylink.cloud.sdk.servers.services.domain.template;

import com.centurylink.cloud.sdk.servers.services.domain.os.OperatingSystem;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.TemplatebyDescriptionRef;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.TemplateByNameRef;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.TemplateByOsRef;

import java.util.List;

/**
 * @author ilya.drabenia
 */
public class Template {
    private String name;
    private String description;
    private OperatingSystem os;
    private List<String> capabilities;

    public Template name(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public Template os(OperatingSystem osType) {
        this.os = osType;
        return this;
    }

    public OperatingSystem getOs() {
        return os;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Template description(String description) {
        setDescription(description);
        return this;
    }

    public List<String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<String> capabilities) {
        this.capabilities = capabilities;
    }

    public Template capabilities(List<String> capabilities) {
        setCapabilities(capabilities);
        return this;
    }

    public static TemplateByNameRef refByName() {
        return new TemplateByNameRef(null, null);
    }

    public static TemplateByOsRef refByOs() {
        return new TemplateByOsRef(null, null, null, null, null);
    }

    public static TemplatebyDescriptionRef refByDescription() {
        return new TemplatebyDescriptionRef(null, null);
    }
}
