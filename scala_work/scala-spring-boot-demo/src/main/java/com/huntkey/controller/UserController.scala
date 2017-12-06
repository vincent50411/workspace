package com.huntkey.controller

import com.huntkey.service.UserService
import com.huntkey.vo.{BaseUser, ChildUserInfo}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{PathVariable, RequestMapping, RequestParam, RestController}

/**
  * Created by liuwens on 2017/10/20.
  */
@RestController
class UserController
{

  @Autowired
  private val userService:UserService[ChildUserInfo] = null;

  @RequestMapping(value = Array("/test/{userName}"))
  def userFind(@PathVariable("userName")userName:String, @RequestParam userAge:Int): Unit =
  {

    val list = List("abc", "bcd", "efg", "hij");

    val map = list.groupBy(new String(_));//list.groupBy((key:String) => key);

    val fun = (key:String, values:List[String]) => println("sdfsdf")

    map.foreach((value:Tuple2[String, List[String]]) =>
    {
      println("value:" + value._2.hashCode())
    });

    val baseUser:BaseUser = userService.getUserInfo("liuwensheng");


    for(index <- 0 to (list.length - 1); if index % 3 == 0)
      {
        print("user name is :%s", userName, index);
      }





    val person = Student("Lilei", 20);


  }

  def fun1(userName:String, age:Int):Unit = {
    val person = Student("Lilei", 20);
  }


  abstract  class Person;

  case class Student(studentName:String, studentAge:Int);
}
