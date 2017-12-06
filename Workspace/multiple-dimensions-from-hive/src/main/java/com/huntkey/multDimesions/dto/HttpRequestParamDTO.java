package com.huntkey.multDimesions.dto;

/**
 * Created by liuwens on 2017/8/8.
 */
public class HttpRequestParamDTO
{
    //rest api访问相对路径
    private String restAPI;

    //请求参数
    private String inputJSONParam;

    private String serverURL;

    private String authMessage;

    private String contetType;


    public String getRestAPI() {
        return restAPI;
    }

    public void setRestAPI(String restAPI) {
        this.restAPI = restAPI;
    }

    public String getInputJSONParam() {
        return inputJSONParam;
    }

    public void setInputJSONParam(String inputJSONParam) {
        this.inputJSONParam = inputJSONParam;
    }

    public String getServerURL() {
        return serverURL;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    public String getAuthMessage() {
        return authMessage;
    }

    public void setAuthMessage(String authMessage) {
        this.authMessage = authMessage;
    }

    public String getContetType() {
        return contetType;
    }

    public void setContetType(String contetType) {
        this.contetType = contetType;
    }
}
