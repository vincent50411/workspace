package com.company;

import com.company.function.FunctionTest_01;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main
{

    public static void main(String[] args)
    {
        Main main = new Main();

        main.test1();
    }

    private void test1()
    {
        String[] args = new String[]{"test", "sdfsd", "sdfdsfdsfds", "sdfdsfdsfdsfdsf"};

        List<String> stringList =Arrays.asList(args);

        FunctionTest_01 action = (List<String> things) -> {
            things.forEach(System.out::println);

            String result = things.stream().filter(item -> {
                if("test".equals(item))
                {
                    return true;
                }
                else if("sdfsd".equals(item))
                {
                    return true;
                }
                else if("sdfdsfdsfds".equals(item))
                {
                    return true;
                }
                return false;
            }).collect(Collectors.joining("-"));

            return result;
        };

        String result = action.doSomething(stringList);

        System.out.println("-> result:" + result);

    }

    public static void excuteDoSomething(FunctionTest_01 action, List<String> things)
    {
        String result = action.doSomething(things);

        System.out.println("-> result:" + result);
    }

}
