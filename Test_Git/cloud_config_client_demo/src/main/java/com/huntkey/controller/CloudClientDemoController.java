package com.huntkey.controller;

import com.huntkey.config.MysqlConfig;
import com.huntkey.config.TestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuwens on 2017/8/24.
 */
@RestController
public class CloudClientDemoController
{
    @Autowired
    private MysqlConfig mysqlConfig;

    @Autowired
    private TestConfig testConfig;

    @RequestMapping("/test")
    public String getMySQLUrl()
    {

        return testConfig.getTest() + "; hello:" + testConfig.getHello();
    }
}
