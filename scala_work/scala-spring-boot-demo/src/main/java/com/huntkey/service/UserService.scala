package com.huntkey.service

import com.huntkey.vo.BaseUser
import org.springframework.stereotype.Service

/**
  * Created by liuwens on 2017/10/21.
  */
@Service
class UserService[A]
{

  def getUserInfo[U <: A](userName:String): BaseUser =
  {
    val baseUser:BaseUser = new BaseUser(userName);

    return baseUser;
  }

  def getUserInfo[U <: A](value:A):A =
  {

    return value;
  }



}
