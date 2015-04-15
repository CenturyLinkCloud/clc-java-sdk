package com.centurylink.cloud.sdk.sample.port.adapter.web;

import com.centurylink.cloud.sdk.sample.port.adapter.web.beans.DataCenterBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author ilya.drabenia
 */
@RestController
@RequestMapping("/datacenter")
public class DataCenterController {

    @RequestMapping(method = GET)
    List<DataCenterBean> findAll() {
        return Arrays.asList(
            new DataCenterBean("de1", "DE1 - Germany (Frankfurt)"),
            new DataCenterBean("gb1", "GB1 - Great Britain (Portsmouth)"),
            new DataCenterBean("il1", "IL1 - US Central (Chicago)")
        );
    }

}
