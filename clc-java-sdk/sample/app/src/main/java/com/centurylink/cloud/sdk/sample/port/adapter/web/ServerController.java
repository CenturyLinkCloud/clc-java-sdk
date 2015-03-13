package com.centurylink.cloud.sdk.sample.port.adapter.web;

import com.centurylink.cloud.sdk.sample.domain.SdkRegistry;
import com.centurylink.cloud.sdk.sample.port.adapter.web.beans.ServerBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author ilya.drabenia
 */
@RestController
@RequestMapping("/datacenter/{dataCenter}/server")
public class ServerController {

    @Autowired
    SdkRegistry sdkRegistry;

    @RequestMapping(method = POST)
    public ServerBean createServer(@RequestBody ServerBean serverBean) {
        return new ServerBean(
            sdkRegistry.findOrCreate("idrabenia", "RenVortEr9")
                    .serverService()
                    .create(serverBean.getServer())
                    .getResult()
        );
    }

}
