package com.huntkey.service;

import com.huntkey.dto.HttpResultDto;

/**
 * HTTP访问接口，定义Rest风格下的调用接口
 * Created by liuwens on 2017/8/7.
 */

public interface HttpRequestService
{
    /**
     * 发送HTTP REST POST请求
     * @param restURL API的相对请求地址
     * @param inputParam json字符串入参
     * @return
     */
    HttpResultDto httpPost(String restURL, String inputParam);

}
