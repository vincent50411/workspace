package com.huntkey.service

import org.springframework.stereotype.Service

/**
  * Created by liuwens on 2017/9/14.
  */
@Service(value = "UserService")
class UserService {

  def getUserName(userName:String):String =
  {

    return userName.concat("你好")

  }

}
