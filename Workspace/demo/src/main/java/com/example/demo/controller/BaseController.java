package com.example.demo.controller;

import com.example.demo.dto.HttpResultDTO;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.PathVariable;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuwens on 2017/7/14.
 */
public class BaseController
{

    public HttpResultDTO authorization()
    {
        HttpResultDTO httpResultDTO = null;

        String basicAuthStr = getbasicAuthStr();

        try
        {
            httpResultDTO = getPostResponse(basicAuthStr);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return httpResultDTO;

    }

    protected String getbasicAuthStr()
    {
        String authStr = "ADMIN:KYLIN";

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

    /**
     * post方式提交表单（模拟用户登录请求）
     */
    public HttpResultDTO getPostResponse(String basicAuthStr)
    {

        HttpResultDTO httpResultDTO = new HttpResultDTO();

        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost("http://192.168.13.35:7070/kylin/api/user/authentication");

        httppost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + basicAuthStr);
        httppost.setHeader("Content-Type", "application/json;charset=UTF-8");

        // 创建参数队列
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("username", "admin"));
        formparams.add(new BasicNameValuePair("password", "admin"));
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);

            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);

            int httpStatusCode = response.getStatusLine().getStatusCode();

            httpResultDTO.setStatusCode(httpStatusCode);

            responseResult(httpResultDTO, response);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return httpResultDTO;
    }

    protected void responseResult(HttpResultDTO httpResultDTO, CloseableHttpResponse response) throws IOException {
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
