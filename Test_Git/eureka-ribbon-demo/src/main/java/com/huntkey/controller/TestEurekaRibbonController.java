package com.huntkey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by liuwens on 2017/8/18.
 */
@RestController
public class TestEurekaRibbonController
{

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add()
    {
        return restTemplate.getForEntity("http://COMPUTE-SERVICE/test?param1=10&param2=20", String.class).getBody();
    }

}
