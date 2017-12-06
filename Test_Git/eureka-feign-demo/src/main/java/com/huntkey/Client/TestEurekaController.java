package com.huntkey.Client;

import com.huntkey.DTO.UserInfoDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by liuwens on 2017/8/18.
 */
@FeignClient(value = "compute-service")
public interface TestEurekaController
{

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    String test(@RequestParam("param1") int param1, @RequestParam("param2") int param2);

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    String addUserInfo(@RequestParam("userName") String userName, @RequestParam("age") int age);

    /**
     * post请求参数注解应该使用@RequestBody
     * 实体类bean中的属性和json字段的转换需要设置
     * @param userInfoDTO
     * @return
     */
    @RequestMapping(value = "/user2", method = RequestMethod.POST)
    UserInfoDTO addUserInfo2(@RequestBody UserInfoDTO userInfoDTO);
}
