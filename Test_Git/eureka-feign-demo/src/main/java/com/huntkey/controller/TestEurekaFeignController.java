package com.huntkey.controller;

import com.huntkey.Client.TestEurekaController;
import com.huntkey.DTO.UserInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by liuwens on 2017/8/18.
 */
@RestController
public class TestEurekaFeignController
{

    @Autowired
    private TestEurekaController testEurekaController;

    @RequestMapping("/add")
    public String testAdd(@RequestParam int param1, @RequestParam int param2)
    {

        return testEurekaController.test(param1, param2);
    }

    @RequestMapping("/user")
    public String getUserInfo(@RequestParam String userName, @RequestParam int age)
    {

        return testEurekaController.addUserInfo(userName, age);
    }

    @RequestMapping(value = "/user2", method =RequestMethod.POST)
    public UserInfoDTO saveUserInfo(@RequestBody UserInfoDTO userInfoDTO)
    {
        return testEurekaController.addUserInfo2(userInfoDTO);
    }

}
