package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dto.HttpResultDTO;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.UUID;

/**
 * Created by liuwens on 2017/7/14.
 */
@RestController
@RequestMapping("/kylin/model")
public class KylinConfigModelController extends BaseController
{

    @RequestMapping("/dataModel/{projectName}/{modelName}")
    public String createDataModel(@PathVariable("projectName") String projectName, @PathVariable("modelName") String modelName)
    {
        //鉴权
        HttpResultDTO httpResultDTO = authorization();

        if(httpResultDTO.getStatusCode() == HttpURLConnection.HTTP_OK)
        {
            //创建dataModel, 发送post请求
            String createDataModelURL = "http://192.168.13.35:7070/kylin/api/models";

            httpResultDTO = sendPost(createDataModelURL, projectName, modelName);

        }

        return httpResultDTO.getResultMessage();

    }

    public HttpResultDTO sendPost(String url, String projectName, String modelName)
    {
        String basicAuthStr = getbasicAuthStr();

        HttpResultDTO httpResultDTO = new HttpResultDTO();

        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost(url);

        httppost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + basicAuthStr);
        httppost.setHeader("Content-Type", "application/json;charset=UTF-8");

        try {

            JSONObject jsonParam = new JSONObject();

            jsonParam.put("modelName", modelName);
            jsonParam.put("project", projectName);


            JSONObject modelDescData = new JSONObject();
            modelDescData.put("uuid", UUID.randomUUID().toString());

            //新增model时last_modified=0
            modelDescData.put("last_modified", "0");
            modelDescData.put("name", modelName);
            modelDescData.put("owner", "ADMIN");
            modelDescData.put("description", "111111111111");
            modelDescData.put("fact_table", "DEFAULT.HIVE_T_HK_SALES");

            //lookups list
            JSONArray lookupsTableArray = new JSONArray();

            //岗位维度表
            JSONObject gwTable = new JSONObject();
            gwTable.put("table", "DEFAULT.HIVE_T_HK_GW");

            JSONObject join = new JSONObject();
            join.put("type", "inner");

            JSONArray primaryKeyArray = new JSONArray();
            primaryKeyArray.add("PK_COL");
            join.put("primary_key", primaryKeyArray);

            JSONArray foreignKeyArray = new JSONArray();
            foreignKeyArray.add("FK_GW");
            join.put("foreign_key", foreignKeyArray);

            gwTable.put("join", join);

            lookupsTableArray.add(gwTable);

            modelDescData.put("lookups", lookupsTableArray);

            //dimensions
            JSONArray dimensionsArray = new JSONArray();

            //事实表
            JSONObject dimensionFactTable = new JSONObject();

            dimensionFactTable.put("table", "DEFAULT.HIVE_T_HK_SALES");

            JSONArray factColumnsArraly = new JSONArray();
            factColumnsArraly.add("FK_GW");

            dimensionFactTable.put("columns", factColumnsArraly);

            //维度表
            JSONObject dimensionGWTable = new JSONObject();

            dimensionGWTable.put("table", "DEFAULT.HIVE_T_HK_GW");

            JSONArray gwColumnsArraly = new JSONArray();
            gwColumnsArraly.add("PK_COL");

            dimensionGWTable.put("columns", gwColumnsArraly);

            dimensionsArray.add(dimensionFactTable);
            dimensionsArray.add(dimensionGWTable);
            modelDescData.put("dimensions", dimensionsArray);


            JSONArray metricsArray = new JSONArray();
            metricsArray.add("SALES_VALUE");
            modelDescData.put("metrics", metricsArray);

            modelDescData.put("filter_condition", "");

            JSONObject partitionDesc = new JSONObject();
            partitionDesc.put("partition_date_column", "DEFAULT.HIVE_T_HK_SALES.SALES_TIME");
            partitionDesc.put("partition_time_column", "null");
            partitionDesc.put("partition_date_start", "0");
            partitionDesc.put("partition_date_format", "yyyy-MM-dd");
            partitionDesc.put("partition_time_format", "HH:mm:ss");
            partitionDesc.put("partition_type", "APPEND");
            partitionDesc.put("partition_condition_builder", "org.apache.kylin.metadata.model.PartitionDesc$DefaultPartitionConditionBuilder");
            modelDescData.put("partition_desc", partitionDesc);

            modelDescData.put("capacity", "MEDIUM");

            String teststr = modelDescData.toString();

            teststr = teststr.replaceAll("\"", "\\\"");
            teststr = teststr.replaceAll("[\r\n]", " ");
            teststr = teststr.trim();

            //modelDescDataStr
            jsonParam.put("modelDescData", teststr);

            System.out.println(jsonParam.toString());


            StringEntity jsonEntity = new StringEntity(jsonParam.toString(),"utf-8");

            httppost.setEntity(jsonEntity);

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
        }
        catch (JSONException jse)
        {
            jse.printStackTrace();
        }
        finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return httpResultDTO;
    }




}
