package com.huntkey.multDimesions.service.http;


import com.huntkey.multDimesions.dto.HttpRequestParamDTO;
import com.huntkey.multDimesions.dto.HttpResultDTO;

/**
 * HTTP访问接口，定义Rest风格下的调用接口
 * Created by liuwens on 2017/8/7.
 */

public interface HttpRequestService
{

    /**
     * 发送HTTP REST GET请求
     * @param param 请求参数
     * @return
     */
    HttpResultDTO httpGet(HttpRequestParamDTO param);

    /**
     * 发送HTTP REST POST请求
     * @param param 请求参数
     * @return
     */
    HttpResultDTO httpPost(HttpRequestParamDTO param);

    /**
     * 发送HTTP REST DELETE请求
     * @param param
     * @return
     */
    HttpResultDTO httpDelete(HttpRequestParamDTO param);


    /**
     * 发送HTTP REST PUT请求
     * @param param
     * @return
     */
    HttpResultDTO httpPut(HttpRequestParamDTO param);

}
