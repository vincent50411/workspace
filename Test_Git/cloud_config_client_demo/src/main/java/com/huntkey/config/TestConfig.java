package com.huntkey.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by liuwens on 2017/8/24.
 */
@Configuration
public class TestConfig
{
    @Value("${info.foo}")
    private String hello;

    @Value("${test}")
    private String test;

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello=hello;
    }


    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test=test;
    }
}
