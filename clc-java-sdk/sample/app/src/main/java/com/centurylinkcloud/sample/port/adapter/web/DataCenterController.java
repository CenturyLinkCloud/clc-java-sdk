package com.centurylinkcloud.sample.port.adapter.web;

import com.centurylinkcloud.sample.port.adapter.web.beans.DataCenter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    List<DataCenter> findAll() {
        return Arrays.asList(
            new DataCenter("DE1", "DE1 - Germany (Frankfurt)"),
            new DataCenter("GB1", "GB1 - Great Britain (Portsmouth)"),
            new DataCenter("IL1", "IL1 - US Central (Chicago)")
        );
    }

}
