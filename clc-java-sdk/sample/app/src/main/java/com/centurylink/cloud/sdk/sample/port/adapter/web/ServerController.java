package com.centurylink.cloud.sdk.sample.port.adapter.web;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.sample.domain.SdkRegistry;
import com.centurylink.cloud.sdk.sample.port.adapter.web.beans.ServerBean;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author ilya.drabenia
 */
@RestController
@RequestMapping("/datacenter/{dataCenter}/server")
public class ServerController {

    @Autowired
    SdkRegistry sdkRegistry;

    @RequestMapping(method = GET)
    public List<ServerMetadata> getAll(@PathVariable String dataCenter) {
        return sdkRegistry.findOrCreate("idrabenia", "RenVortEr9")
            .serverService().find(new ServerFilter().dataCenters(DataCenter.refById(dataCenter)));
    }

    @RequestMapping(method = POST)
    public ServerBean createServer(@RequestBody ServerBean serverBean) {
        sdkRegistry.findOrCreate("idrabenia", "RenVortEr9")
                .serverService()
                .create(serverBean.getServer())
                .getResult();

        return serverBean;
    }

}
