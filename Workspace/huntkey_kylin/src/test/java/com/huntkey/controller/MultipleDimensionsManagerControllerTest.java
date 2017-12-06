package com.huntkey.controller;

import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by liuwens on 2017/8/7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MultipleDimensionsManagerControllerTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;


    @Before
    public void setUp() throws Exception
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void test1() throws Exception
    {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/huntkeyKylin/test");
        mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(content().string("{\"server\":\"http://192.168.13.35:7070/kylin\",\"user\":\"ADMIN\",\"password\":\"KYLIN\",\"contetType\":\"UTF-8\"}"));

        System.out.println("test1 end");
    }

    @Test
    public void saveKylinProjectTest() throws Exception
    {
        JSONObject param = new JSONObject();
        param.put("formerProjectName", "Test_Kylin_Porject_001");
        param.put("projectDescData", "Test_Kylin_Porject_001");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/huntkeyKylin/project").
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(param.toJSONString());
        mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.resultStatus").value(true));

        System.out.println("saveKylinProjectTest end");
    }

}