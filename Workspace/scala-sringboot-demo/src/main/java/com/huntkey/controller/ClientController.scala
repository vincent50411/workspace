package com.huntkey.controller

import com.huntkey.service.UserService
import org.springframework.beans.factory.annotation.{Autowired, Qualifier}
import org.springframework.web.bind.annotation.{RequestMapping, RestController}

/**
  * Created by liuwens on 2017/9/14.
  */
@RestController
@RequestMapping(value = Array("/scals"))
class ClientController {

  @Autowired
  @Qualifier(value = "UserService")
  var userService:UserService = null;

  @RequestMapping(Array("/test"))
  def userTest(): String =
  {

    return "My is scala controller; message from " + userService.getUserName("李四, ");

  }

}
