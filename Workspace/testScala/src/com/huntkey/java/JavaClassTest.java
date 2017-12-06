package com.huntkey.java;

import com.huntkey.scala.TestScalaMain;

/**
 * Created by liuwens on 2017/8/1.
 */
public class JavaClassTest
{

    public static void main(String[] args)
    {
        TestScalaMain.scalaTestFunction("我是从java端调用scala端的方法启动scala应用的");



    }

    public String testJavaBean(String userName)
    {

        return userName + ", I'm from java class method return.";
    }


}
