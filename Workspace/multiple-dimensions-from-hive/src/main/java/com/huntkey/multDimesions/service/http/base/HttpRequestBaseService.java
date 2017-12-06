package com.huntkey.multDimesions.service.http.base;

import com.huntkey.multDimesions.dto.HttpRequestParamDTO;
import com.huntkey.multDimesions.dto.HttpResultDTO;
import com.huntkey.multDimesions.service.http.HttpRequestService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by liuwens on 2017/8/11.
 */
public abstract class HttpRequestBaseService implements HttpRequestService
{
    @Override
    public HttpResultDTO httpPost(HttpRequestParamDTO inputParam)
    {
        HttpResultDTO httpResultDTO = new HttpResultDTO();

        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // 创建httppost
        HttpPost httppost = new HttpPost(inputParam.getRestAPI());
        httppost.setHeader("Content-Type", inputParam.getContetType());

        if(inputParam != null && inputParam.getInputJSONParam() != null)
        {
            //设置POST入参
            StringEntity jsonEntity = new StringEntity(inputParam.getInputJSONParam(), "utf-8");

            httppost.setEntity(jsonEntity);
        }

        //执行Http请求
        httpExecute(httpResultDTO, httpclient, httppost);

        return httpResultDTO;
    }

    protected void httpExecute(HttpResultDTO httpResultDTO, CloseableHttpClient httpclient, HttpRequestBase httpRequestBase)
    {
        try
        {
            CloseableHttpResponse response = httpclient.execute(httpRequestBase);

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
