package com.example.demo.controller;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dto.HttpResultDTO;
import com.example.demo.util.DateUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuwens on 2017/6/28.
 */
@RestController
@EnableAutoConfiguration
public class KylinClientController extends BaseController {

    @RequestMapping(value = "/authentication/{startTime}/{endTime}", method = RequestMethod.GET)
    public String authorization(@PathVariable("startTime")String startTimeValue, @PathVariable("endTime")String endTimeValue)
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

        //Rest API调用
        String kylinAuthPath = "http://192.168.13.35:7070/kylin/api/user/authentication";

        String resultMessage = "";
        try
        {
            HttpResultDTO httpResultDTO = getPostResponse(basicAuthStr);

            if(httpResultDTO.getStatusCode() == HttpURLConnection.HTTP_OK)
            {
                String cubeName = "Test_Cube_009_001";
                String getCubeInfoURL = "http://192.168.13.35:7070/kylin/api/cubes?cubeName=" + cubeName + "&limit=15&offset=0";

                //HttpResultDTO cubeInfoResult = sendHttpGetRequest(getCubeInfoURL, basicAuthStr);

                String bulidCubeURL = "http://192.168.13.35:7070/kylin/api/cubes/" + cubeName + "/rebuild";
                HttpResultDTO buildCubeResult = sendHttppUTRequest(bulidCubeURL, basicAuthStr, startTimeValue, endTimeValue);

                resultMessage = buildCubeResult.getResultMessage();
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return resultMessage;

    }

    /**
     * 构建指定的cube任务
     * "yyyy-MM-dd","yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss",
     * @param cubeName
     * @param startTimeValue
     * @param endTimeValue
     * @return
     */
    @RequestMapping("/build/{cubeName}/{startTime}/{endTime}")
    public String buildCube(@PathVariable("cubeName")String cubeName, @PathVariable("startTime")String startTimeValue, @PathVariable("endTime")String endTimeValue)
    {
        String resultMessage = "";
        try
        {
            //鉴权
            HttpResultDTO httpResultDTO = authorization();

            if(httpResultDTO.getStatusCode() == HttpURLConnection.HTTP_OK)
            {
                String basicAuthStr = getbasicAuthStr();

                String bulidCubeURL = "http://192.168.13.35:7070/kylin/api/cubes/" + cubeName + "/build";
                HttpResultDTO buildCubeResult = sendHttppUTRequest(bulidCubeURL, basicAuthStr, startTimeValue, endTimeValue);

                resultMessage = buildCubeResult.getResultMessage();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return resultMessage;
    }





    public HttpResultDTO sendHttpGetRequest(String restURL, String basicAuthStr)
    {
        HttpResultDTO httpResultDTO = new HttpResultDTO();

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try
        {
            // 创建httpget.
            HttpGet httpget = new HttpGet(restURL);
            httpget.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + basicAuthStr);
            httpget.setHeader("Content-Type", "application/json;charset=UTF-8");

            // 执行get请求.
            CloseableHttpResponse response = httpclient.execute(httpget);

            httpResultDTO.setStatusCode(response.getStatusLine().getStatusCode());

            try
            {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                System.out.println("--------------------------------------");

                // 打印响应状态
                System.out.println(response.getStatusLine());
                if (entity != null)
                {
                    // 打印响应内容长度
                    System.out.println("Response content length: " + entity.getContentLength());

                    String message = EntityUtils.toString(entity);

                    // 打印响应内容
                    System.out.println("Response content: " + message);

                    httpResultDTO.setResultMessage(message);
                }
                System.out.println("------------------------------------");
            }
            finally
            {
                response.close();
            }
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return httpResultDTO;
    }

    public HttpResultDTO sendHttppUTRequest(String restURL, String basicAuthStr, String startTime, String endTime)
    {
        HttpResultDTO httpResultDTO = new HttpResultDTO();

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try
        {
            // 创建httpget.
            HttpPut httpPut = new HttpPut(restURL);
            httpPut.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + basicAuthStr);
            httpPut.setHeader("Content-Type", "application/json;charset=UTF-8");

            CloseableHttpResponse response = null;

            try
            {
                System.out.println("startTime:" + startTime + ", endTime:" + endTime);

                JSONObject jsonParam = new JSONObject();
                //"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss",
                jsonParam.put("startTime", DateUtils.parseDate(startTime).getTime());
                jsonParam.put("endTime", DateUtils.parseDate(endTime).getTime());
                jsonParam.put("buildType", "BUILD");

                //解决中文乱码问题
                StringEntity jsonEntity = new StringEntity(jsonParam.toString(),"utf-8");

                httpPut.setEntity(jsonEntity);

                // 执行get请求.
                response = null;//httpclient.execute(httpPut);

                httpResultDTO.setStatusCode(response.getStatusLine().getStatusCode());

                // 获取响应实体
                HttpEntity entity = response.getEntity();
                System.out.println("--------------------------------------");

                // 打印响应状态
                System.out.println(response.getStatusLine());
                if (entity != null)
                {
                    // 打印响应内容长度
                    System.out.println("Response content length: " + entity.getContentLength());

                    String message = EntityUtils.toString(entity);

                    // 打印响应内容
                    System.out.println("Response content: " + message);

                    httpResultDTO.setResultMessage(message);
                }
                System.out.println("------------------------------------");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            finally
            {
                response.close();
            }
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return httpResultDTO;
    }


    @RequestMapping("/sql")
    public String queryData()
    {
        String resultStr = null;

        String sql = "select sum(a.sales_value) from hive_t_hk_sales a left join hive_t_hk_dept b on a.fk_dept = b.pk_col left join kylin_cal_dt c on a.sales_time = c.cal_dt group by c.year_beg_dt, c.qtr_beg_dt, c.month_beg_dt,b.dept_code_0";

        //鉴权
        HttpResultDTO authorizationResult = authorization();

        if(authorizationResult.getStatusCode() == HttpURLConnection.HTTP_OK)
        {
            HttpResultDTO httpResultDTO = new HttpResultDTO();

            try
            {
                String basicAuthStr = getbasicAuthStr();

                // 创建默认的httpClient实例.
                CloseableHttpClient httpclient = HttpClients.createDefault();
                // 创建httppost
                HttpPost httppost = new HttpPost("http://192.168.13.35:7070/kylin/api/query");

                httppost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + basicAuthStr);
                httppost.setHeader("Content-Type", "application/json;charset=UTF-8");

                JSONObject jsonParam = new JSONObject();

                jsonParam.put("sql", sql);
                jsonParam.put("offset", 0);
                jsonParam.put("limit", 50000);
                jsonParam.put("acceptPartial", false);
                jsonParam.put("project", "3_dimesion_project");

                StringEntity jsonEntity = new StringEntity(jsonParam.toString(),"utf-8");

                httppost.setEntity(jsonEntity);

                CloseableHttpResponse response = httpclient.execute(httppost);

                int httpStatusCode = response.getStatusLine().getStatusCode();

                httpResultDTO.setStatusCode(httpStatusCode);

                responseResult(httpResultDTO, response);

                resultStr = httpResultDTO.getResultMessage();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return resultStr;

    }






}
