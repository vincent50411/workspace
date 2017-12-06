package com.huntkey.scala

import com.huntkey.java.JavaClassTest

/**
  * Created by liuwens on 2017/8/1.
  */
object TestScalaMain {

  def main(argsxx: Array[String]) {
    //println("sfdsfdsf" + argsxx(0) + " " + argsxx(1));

    val test = testFun(f, "sdfsdfsdfdsf");

    println(test)

    var javaClassTest: JavaClassTest = new JavaClassTest();
    println(javaClassTest.testJavaBean("刘文生"))

    delayed(time());


    functionX(1000);
  }

  def scalaTestFunction(message: String) = {
    println(message);
  }


  def testFun(fun: String => String, name: String) = {

    println("此处应该执行一些业务逻辑，比如获取网上数据，或者数据库内容")

    print("获取到了很多数据后，执行回调函数调用")

    var result = DataService.getUserInfoService("ssssss")

    fun("已经得到了用户信息:" + result);
  }

  def f(name: String): String = {
    return name;
  }


  def funA(index: Int): Int = {
    var testFunction = (x: Int) => println(x + 100);

    testFunction(index);

    return 1;

  }


  def time() = {
    println("获取时间，单位为纳秒")
    System.nanoTime
  }

  def delayed(t: => Long) = {
    println("在 delayed 方法内")
    println("参数： " + t)
    t
  }


  def functionX(x: Int) =
  {

    def tet(tt:String) =
    {
      (x: Int) => x + ";test; " + tt;
    }

    println(tet("test main ")(1111));

    refFunction(callBackA, "成功啦!")

  }

  def refFunction(fun:(String, Boolean) => Unit, message:String) : Unit =
  {
    println("----------------------")

    fun(message, true)

    println("------------2----------")
  }






  def callBackA(data:String, status:Boolean) =
  {
    if(status)
    {
      println("执行成功, result:" + data)
    }
    else{
      println("执行失败: result:" + data)
    }




  }



}
