package com.huntkey.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.huntkey.dto.HttpResultDto;
import com.huntkey.service.HttpRequestService;
import com.huntkey.service.MultipleDimensionsManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by liuwens on 2017/8/7.
 */
@Service(value = "MultipleDimensionsManagerServiceImpl")
public class MultipleDimensionsManagerServiceImpl extends KylinRequestService implements MultipleDimensionsManagerService
{
    @Autowired
    @Qualifier(value = "KylinRequestService")
    private HttpRequestService kylinRequestService;


    public boolean kylinAuthorization()
    {
        HttpResultDto httpResultDto = kylinRequestService.httpPost("/api/user/authentication", null);

        return httpResultDto.isResultStatus();
    }

    public HttpResultDto saveKylinProject(String formerProjectName, String projectDescData)
    {
        //组装参数
        JSONObject jsonParam = new JSONObject();

        jsonParam.put("name", formerProjectName);
        jsonParam.put("description", projectDescData);

        HttpResultDto httpResultDto = kylinRequestService.httpPost("/api/projects", jsonParam.toJSONString());

        return httpResultDto;
    }




}
