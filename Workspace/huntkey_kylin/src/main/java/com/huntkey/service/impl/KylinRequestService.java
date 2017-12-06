package com.huntkey.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.huntkey.config.ApplicationLocalProperties;
import com.huntkey.dto.HttpResultDto;
import com.huntkey.dto.KylinServerInfoDto;
import com.huntkey.service.HttpRequestService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

/**
 * 实现调用kylin API的请求服务
 * Created by liuwens on 2017/8/7.
 */
@Service(value = "KylinRequestService")
public class KylinRequestService implements HttpRequestService
{

    @Autowired
    private ApplicationLocalProperties applicationLocalProperties;

    protected String getKylinServer()
    {
        KylinServerInfoDto kylin = applicationLocalProperties.getKylin();

        return kylin.getServer();
    }

    /**
     * 获取BASE64鉴权码
     * @return
     */
    protected String getbasicAuthStr()
    {
        KylinServerInfoDto kylin = applicationLocalProperties.getKylin();

        String authStr = kylin.getUser() + ":" + kylin.getPassword();//"ADMIN:KYLIN";

        byte[] b = null;
        String basicAuthStr = null;
        try
        {
            b = authStr.getBytes("utf-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        if (b != null)
        {
            basicAuthStr = new BASE64Encoder().encode(b);
        }

        return basicAuthStr;

    }

    @Override
    public HttpResultDto httpPost(String restURL, String inputParam)
    {
        HttpResultDto httpResultDTO = new HttpResultDto();

        //BASE64鉴权字符串
        String basicAuthStr = getbasicAuthStr();

        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();

        String urlPath = getKylinServer() + restURL;

        // 创建httppost
        HttpPost httppost = new HttpPost(urlPath);

        httppost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + basicAuthStr);
        httppost.setHeader("Content-Type", applicationLocalProperties.getKylin().getContetType());

        try
        {
            if(inputParam != null)
            {
                //设置POST入参
                StringEntity jsonEntity = new StringEntity(inputParam,"utf-8");

                httppost.setEntity(jsonEntity);
            }

            //发起POST请求
            CloseableHttpResponse response = httpclient.execute(httppost);

            int httpStatusCode = response.getStatusLine().getStatusCode();

            if(HttpURLConnection.HTTP_OK == httpStatusCode)
            {
                httpResultDTO.setResultStatus(true);
            }
            else
            {
                httpResultDTO.setResultStatus(false);
            }

            responseResult(httpResultDTO, response);
        }
        catch (Exception ex)
        {
            httpResultDTO.setResultStatus(false);
            httpResultDTO.setResultMessage(ex.getMessage());

            ex.printStackTrace();
        }

        return httpResultDTO;
    }

    protected void responseResult(HttpResultDto httpResultDTO, CloseableHttpResponse response) throws IOException {
        try {
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                System.out.println("--------------------------------------");
                String resultMessage = EntityUtils.toString(entity, "UTF-8");

                httpResultDTO.setResultMessage(resultMessage);

                System.out.println("Response content: " + resultMessage);
                System.out.println("--------------------------------------");
            }
        } finally {
            response.close();
        }
    }
}
