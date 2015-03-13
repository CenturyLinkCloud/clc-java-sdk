package com.centurylink.cloud.sdk.sample.port.adapter.web;

import com.centurylink.cloud.sdk.sample.domain.SdkRegistry;
import com.centurylink.cloud.sdk.servers.domain.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author ilya.drabenia
 */
@RestController
@RequestMapping("/datacenter/{dataCenter}/template")
public class TemplateController {

    @Autowired
    SdkRegistry sdkRegistry;

    @RequestMapping(method = GET)
    public List<Template> findByDataCenter(@PathVariable("dataCenter") String dataCenter) {
        return
            sdkRegistry.findOrCreate("idrabenia", "RenVortEr9")
                .templateService()
                .findByDataCenter(dataCenter);
    }

}
