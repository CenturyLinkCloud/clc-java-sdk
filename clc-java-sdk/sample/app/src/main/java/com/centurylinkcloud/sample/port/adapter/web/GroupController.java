package com.centurylinkcloud.sample.port.adapter.web;

import com.centurylinkcloud.ClcSdk;
import com.centurylinkcloud.core.auth.domain.credentials.StaticCredentialsProvider;
import com.centurylinkcloud.sample.application.SdkRegistry;
import com.centurylinkcloud.servers.domain.Group;
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
@RequestMapping("/datacenter/{dataCenter}/group")
public class GroupController {

    @Autowired
    SdkRegistry sdkRegistry;

    @RequestMapping(method = GET)
    public List<Group> findAll(@PathVariable("dataCenter") String dataCenter) {
        return
            sdkRegistry.findOrCreate("idrabenia", "RenVortEr9")
                .groupService()
                .findByDataCenter(dataCenter);
    }

}
