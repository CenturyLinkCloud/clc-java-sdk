package com.centurylinkcloud.sample.port.adapter.web;

import com.centurylinkcloud.ClcSdk;
import com.centurylinkcloud.core.auth.domain.credentials.StaticCredentialsProvider;
import com.centurylinkcloud.sample.port.adapter.web.beans.DataCenterBean;
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
            new DataCenterBean("DE1", "DE1 - Germany (Frankfurt)"),
            new DataCenterBean("GB1", "GB1 - Great Britain (Portsmouth)"),
            new DataCenterBean("IL1", "IL1 - US Central (Chicago)")
        );
    }

}
