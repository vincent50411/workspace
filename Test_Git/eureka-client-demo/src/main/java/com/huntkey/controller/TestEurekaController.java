package com.huntkey.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuwens on 2017/8/18.
 */
@RestController
public class TestEurekaController
{

    @RequestMapping("/test")
    public String test(@RequestParam int param1, @RequestParam int param2)
    {
        System.out.println("param1:" + param1 + "; param2:" + param2);

        return "value=" + param1 + param2;
    }



}
