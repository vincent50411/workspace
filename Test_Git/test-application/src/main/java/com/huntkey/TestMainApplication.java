package com.huntkey;

/**
 * Created by liuwens on 2017/9/6.
 */
public class TestMainApplication
{
    public static void main(String[] args)
    {
        int number = 6;

        int rightNumber = number << 2;

        printInfo(number);
        printInfo(rightNumber);

     }

     /**
       * 输出一个int的二进制数
       * @param num
      */
      private static void printInfo(int num)
      {
             System.out.println("value:" + Integer.toBinaryString(num));
         }





}
