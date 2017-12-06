package com.huntkey.controller;

import com.huntkey.DTO.UserInfoDTO;
import org.springframework.web.bind.annotation.*;

/**
 * Created by liuwens on 2017/8/18.
 */
@RestController
public class TestEurekaController2
{

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public String addUserInfo(@RequestParam String userName, @RequestParam int age)
    {
        System.out.println("--> User Name;" + userName + "; User Age:" + age);

        return "--> User Name;" + userName + "; User Age:" + age;
    }

    @RequestMapping(value = "/user2", method = RequestMethod.POST)
    public UserInfoDTO addUserInfo2(@RequestBody UserInfoDTO userInfoDTO)
    {
        System.out.println("--> userInfoDTO.getUserName():" + userInfoDTO.getUserName() + "; userInfoDTO.getUserAge():" + userInfoDTO.getUserAge());

        return userInfoDTO;
    }



}
