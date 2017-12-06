package com.huntkey.multDimesions.config;

import com.huntkey.multDimesions.dto.HttpRequestParamDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * EDM请求参数
 * Created by liuwens on 2017/8/11.
 */
@Component
public class EDMRequestParamDTO extends HttpRequestParamDTO
{
    //rest api访问相对路径
    private String restAPI;

    @Value("#{ApplicationLocalProperties.edm.server}")
    private String serverURL;

    @Value("#{ApplicationLocalProperties.edm.contetType}")
    private String contetType;

    public String getRestAPI() {
        return restAPI;
    }

    public void setRestAPI(String restAPI) {

        //拼接成HTTP请求的全路径, 如果有其他配置路径，则需要通过设置serverURL来覆盖默认配置
        this.restAPI = getServerURL() + restAPI;
    }

    @Override
    public String getServerURL() {
        return serverURL;
    }

    @Override
    public void setServerURL(String serverURL) {
        this.serverURL=serverURL;
    }

    @Override
    public String getContetType() {
        return contetType;
    }

    @Override
    public void setContetType(String contetType) {
        this.contetType=contetType;
    }

    @Override
    public String toString()
    {
        return "--> Requset URL:" + this.restAPI + "; Input param:" + this.getInputJSONParam();
    }
}
