package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dto.RequestParamDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * Created by liuwens on 2017/9/20.
 */
@RestController
public class TestController
{
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test()
    {
        JSONArray resourceObjectCodeList = saveRedis();

        return resourceObjectCodeList.toJSONString();
    }

    @RequestMapping(value = "/edm", method = RequestMethod.POST, produces = { "application/json" })
    public String createRedisData(@RequestBody RequestParamDTO requestParamDTO)
    {
        JSONArray resourceObjectCodeList = saveRedis();

        return resourceObjectCodeList.toJSONString();
    }

    @RequestMapping(value = "/record", method = RequestMethod.GET)
    public String addNewRecord()
    {
         addNewRecordToRedis();

        return query();
    }

    private String query()
    {
        Jedis jedis = new Jedis("localhost", 6379);

        String result = jedis.hget("statistics", "DEPT_CLASS_CODE");

        System.out.println(result);

        JSONArray jsonArray = JSONArray.parseArray(result);

        jedis.close();

        return jsonArray.toJSONString();
    }

    public JSONArray saveRedis()
    {
        JSONArray resourceObjectCodeList = new JSONArray();

        Jedis jedis = new Jedis("10.3.99.34", 6379);
        //jedis.auth("redis");

        for(int index = 0; index < 10; index++)
        {
            JSONObject recordItem = new JSONObject();

            recordItem.put("resource_class_code", "DEPT_CLASS_CODE");

            String resource_object_code = UUID.randomUUID().toString();
            recordItem.put("resource_object_code", resource_object_code);
            recordItem.put("attribute_group_code", "123456789");
            recordItem.put("period_code", "111111");
            recordItem.put("aggregate_value", getRandomValue());

            resourceObjectCodeList.add(resource_object_code);

            //jedis.del("111111");
            jedis.lpush("statistics", recordItem.toJSONString());
        }

        //返回的是资源类的所有记录列表
        String result = null;//jedis.hget("statistics", "DEPT_CLASS_CODE");

        /*System.out.println(result);

        JSONArray jsonArray = JSONArray.parseArray(result);

        for (Object obj:jsonArray) {
            JSONObject jsonObject = (JSONObject)obj;

            String resource_object_code = jsonObject.getString("resource_object_code");

            System.out.println(resource_object_code);
        }*/

        //System.out.println(JSONObject.parseObject(result).get("resource_object_code"));

        jedis.close();

        return resourceObjectCodeList;
    }


    private String addNewRecordToRedis()
    {
        Jedis jedis = new Jedis("localhost", 6379);

        Map<String, String> statictisTable = new HashMap();

        JSONObject recordItem = new JSONObject();

        String resource_object_code = UUID.randomUUID().toString();
        recordItem.put("resource_object_code", resource_object_code);
        recordItem.put("attribute_group_code", "123456789");
        recordItem.put("period_code", "2222");
        recordItem.put("aggregate_value", getRandomValue());

        JSONArray recordListMap = new JSONArray();

        recordListMap.add(recordItem);

        statictisTable.put("DEPT_CLASS_CODE", recordListMap.toString());

        jedis.hmset("statistics", statictisTable);

        return statictisTable.toString();
    }


    private String getRandomValue()
    {
        int max = 200;
        int min = 10;
        Random random = new Random();

        int value = random.nextInt(max)%(max-min+1) + min;

        return String.valueOf(value);
    }


}
