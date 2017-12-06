package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.dto.HttpResultDTO;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.HttpURLConnection;
import java.util.UUID;

/**
 * Created by liuwens on 2017/7/14.
 */
@RestController
@RequestMapping("/kylin/cube")
public class KylinConfigCubeController extends BaseController
{

    @RequestMapping("/add/{projectName}/{modelName}/{cubeName}")
    public String createCube(@PathVariable("projectName") String projectName, @PathVariable("modelName") String modelName, @PathVariable("cubeName") String cubeName)
    {
        //鉴权
        HttpResultDTO httpResultDTO = authorization();

        if(httpResultDTO.getStatusCode() == HttpURLConnection.HTTP_OK)
        {
            //创建dataModel, 发送post请求
            String createDataModelURL = "http://192.168.13.35:7070/kylin/api/cubes";

            httpResultDTO = sendCreateCubeRequest(createDataModelURL, projectName, modelName, cubeName);

        }

        return httpResultDTO.getResultMessage();
    }

    private HttpResultDTO sendCreateCubeRequest(String url, String projectName, String modelName, String cubeName)
    {
        String basicAuthStr = getbasicAuthStr();

        HttpResultDTO httpResultDTO = new HttpResultDTO();

        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost(url);

        httppost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + basicAuthStr);
        httppost.setHeader("Content-Type", "application/json;charset=UTF-8");

        try
        {
            JSONObject jsonParam = new JSONObject();

            //java_config_model
            jsonParam.put("cubeName", cubeName);//java_config_cube_001
            jsonParam.put("project", projectName);//java_config_project

            String cubeDescDataStr = "{\n" +
                    "  \"uuid\": \"" + UUID.randomUUID().toString() + "\",\n" +
                    "  \"last_modified\": 0,\n" +
                    "  \"version\": \"1.6.0\",\n" +
                    "  \"name\": \"" + cubeName + "\",\n" +
                    "  \"model_name\": \"" + modelName + "\",\n" +
                    "  \"description\": \"\",\n" +
                    "  \"null_string\": null,\n" +
                    "  \"dimensions\": [\n" +
                    "    {\n" +
                    "      \"name\": \"PK_COL\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_GW\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"PK_COL\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"COL_LABLE\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_GW\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"COL_LABLE\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"DEPT_CODE_0\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_DEPT\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"DEPT_CODE_0\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"DEPT_CODE_1\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_DEPT\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"DEPT_CODE_1\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"DEPT_CODE_2\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_DEPT\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"DEPT_CODE_2\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"DEPT_CODE_3\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_DEPT\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"DEPT_CODE_3\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"DEPT_CODE_4\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_DEPT\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"DEPT_CODE_4\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"DEPT_CODE_5\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_DEPT\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"DEPT_CODE_5\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"DEPT_CODE_6\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_DEPT\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"DEPT_CODE_6\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"DEPT_CODE_7\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_DEPT\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"DEPT_CODE_7\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"DEPT_CODE_8\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_DEPT\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"DEPT_CODE_8\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"PK_COL\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_EMPLOY\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"PK_COL\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"YEAR_BEG_DT\",\n" +
                    "      \"table\": \"DEFAULT.KYLIN_CAL_DT\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"YEAR_BEG_DT\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"QTR_BEG_DT\",\n" +
                    "      \"table\": \"DEFAULT.KYLIN_CAL_DT\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"QTR_BEG_DT\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"MONTH_BEG_DT\",\n" +
                    "      \"table\": \"DEFAULT.KYLIN_CAL_DT\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"MONTH_BEG_DT\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"WEEK_BEG_DT\",\n" +
                    "      \"table\": \"DEFAULT.KYLIN_CAL_DT\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"WEEK_BEG_DT\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"CUSTOM_TRET_ID_0\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_CUSTOM\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"CUSTOM_TRET_ID_0\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"CUSTOM_TRET_ID_1\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_CUSTOM\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"CUSTOM_TRET_ID_1\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"CUSTOM_TRET_ID_2\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_CUSTOM\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"CUSTOM_TRET_ID_2\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"CUSTOM_TRET_ID_3\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_CUSTOM\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"CUSTOM_TRET_ID_3\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"CUSTOM_TRET_ID_4\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_CUSTOM\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"CUSTOM_TRET_ID_4\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"CUSTOM_TRET_ID_5\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_CUSTOM\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"CUSTOM_TRET_ID_5\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"STORE_TRET_ID_0\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_STORE\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"STORE_TRET_ID_0\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"STORE_TRET_ID_1\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_STORE\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"STORE_TRET_ID_1\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"STORE_TRET_ID_2\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_STORE\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"STORE_TRET_ID_2\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"STORE_TRET_ID_3\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_STORE\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"STORE_TRET_ID_3\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"STORE_TRET_ID_4\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_STORE\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"STORE_TRET_ID_4\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"STORE_TRET_ID_5\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_STORE\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"STORE_TRET_ID_5\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"STORE_TRET_ID_6\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_STORE\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"STORE_TRET_ID_6\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"STORE_TRET_ID_7\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_STORE\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"STORE_TRET_ID_7\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"STORE_TRET_ID_8\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_STORE\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"STORE_TRET_ID_8\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"PROD_TRET_ID_0\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_PROD\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"PROD_TRET_ID_0\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"PROD_TRET_ID_1\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_PROD\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"PROD_TRET_ID_1\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"PROD_TRET_ID_2\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_PROD\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"PROD_TRET_ID_2\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"PROD_TRET_ID_3\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_PROD\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"PROD_TRET_ID_3\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"PROD_TRET_ID_4\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_PROD\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"PROD_TRET_ID_4\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"PROD_TRET_ID_5\",\n" +
                    "      \"table\": \"DEFAULT.HIVE_T_HK_PROD\",\n" +
                    "      \"column\": null,\n" +
                    "      \"derived\": [\n" +
                    "        \"PROD_TRET_ID_5\"\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"measures\": [\n" +
                    "    {\n" +
                    "      \"name\": \"_COUNT_\",\n" +
                    "      \"function\": {\n" +
                    "        \"expression\": \"COUNT\",\n" +
                    "        \"parameter\": {\n" +
                    "          \"type\": \"constant\",\n" +
                    "          \"value\": \"1\",\n" +
                    "          \"next_parameter\": null\n" +
                    "        },\n" +
                    "        \"returntype\": \"bigint\"\n" +
                    "      },\n" +
                    "      \"dependent_measure_ref\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"SUM\",\n" +
                    "      \"function\": {\n" +
                    "        \"expression\": \"SUM\",\n" +
                    "        \"parameter\": {\n" +
                    "          \"type\": \"column\",\n" +
                    "          \"value\": \"SALES_VALUE\",\n" +
                    "          \"next_parameter\": null\n" +
                    "        },\n" +
                    "        \"returntype\": \"decimal\"\n" +
                    "      },\n" +
                    "      \"dependent_measure_ref\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"MIN\",\n" +
                    "      \"function\": {\n" +
                    "        \"expression\": \"MIN\",\n" +
                    "        \"parameter\": {\n" +
                    "          \"type\": \"column\",\n" +
                    "          \"value\": \"SALES_VALUE\",\n" +
                    "          \"next_parameter\": null\n" +
                    "        },\n" +
                    "        \"returntype\": \"double\"\n" +
                    "      },\n" +
                    "      \"dependent_measure_ref\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"MAX\",\n" +
                    "      \"function\": {\n" +
                    "        \"expression\": \"MAX\",\n" +
                    "        \"parameter\": {\n" +
                    "          \"type\": \"column\",\n" +
                    "          \"value\": \"SALES_VALUE\",\n" +
                    "          \"next_parameter\": null\n" +
                    "        },\n" +
                    "        \"returntype\": \"double\"\n" +
                    "      },\n" +
                    "      \"dependent_measure_ref\": null\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"dictionaries\": [],\n" +
                    "  \"rowkey\": {\n" +
                    "    \"rowkey_columns\": [\n" +
                    "      {\n" +
                    "        \"column\": \"FK_GW\",\n" +
                    "        \"encoding\": \"dict\",\n" +
                    "        \"isShardBy\": false\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"column\": \"FK_DEPT\",\n" +
                    "        \"encoding\": \"dict\",\n" +
                    "        \"isShardBy\": false\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"column\": \"FK_EMPLOY\",\n" +
                    "        \"encoding\": \"dict\",\n" +
                    "        \"isShardBy\": false\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"column\": \"SALES_TIME\",\n" +
                    "        \"encoding\": \"date\",\n" +
                    "        \"isShardBy\": false\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"column\": \"FK_CUSTOM\",\n" +
                    "        \"encoding\": \"dict\",\n" +
                    "        \"isShardBy\": false\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"column\": \"FK_STORE\",\n" +
                    "        \"encoding\": \"dict\",\n" +
                    "        \"isShardBy\": false\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  },\n" +
                    "  \"hbase_mapping\": {\n" +
                    "    \"column_family\": [\n" +
                    "      {\n" +
                    "        \"name\": \"F1\",\n" +
                    "        \"columns\": [\n" +
                    "          {\n" +
                    "            \"qualifier\": \"M\",\n" +
                    "            \"measure_refs\": [\n" +
                    "              \"_COUNT_\",\n" +
                    "              \"SUM\",\n" +
                    "              \"MIN\",\n" +
                    "              \"MAX\"\n" +
                    "            ]\n" +
                    "          }\n" +
                    "        ]\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  },\n" +
                    "  \"aggregation_groups\": [\n" +
                    "    {\n" +
                    "      \"includes\": [\n" +
                    "        \"FK_GW\",\n" +
                    "        \"FK_DEPT\",\n" +
                    "        \"FK_EMPLOY\",\n" +
                    "        \"SALES_TIME\",\n" +
                    "        \"FK_CUSTOM\",\n" +
                    "        \"FK_STORE\"\n" +
                    "      ],\n" +
                    "      \"select_rule\": {\n" +
                    "        \"hierarchy_dims\": [],\n" +
                    "        \"mandatory_dims\": [],\n" +
                    "        \"joint_dims\": []\n" +
                    "      }\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"signature\": \"NM3yA1Z0DvS+FSWsc7ck8w==\",\n" +
                    "  \"notify_list\": [],\n" +
                    "  \"status_need_notify\": [\n" +
                    "    \"ERROR\",\n" +
                    "    \"DISCARDED\",\n" +
                    "    \"SUCCEED\"\n" +
                    "  ],\n" +
                    "  \"partition_date_start\": 1296518400000,\n" +
                    "  \"partition_date_end\": 3153600000000,\n" +
                    "  \"auto_merge_time_ranges\": [\n" +
                    "    604800000,\n" +
                    "    2419200000\n" +
                    "  ],\n" +
                    "  \"retention_range\": 0,\n" +
                    "  \"engine_type\": 2,\n" +
                    "  \"storage_type\": 2,\n" +
                    "  \"override_kylin_properties\": {}\n" +
                    "}";

            jsonParam.put("cubeDescData", cubeDescDataStr);

            StringEntity jsonEntity = new StringEntity(jsonParam.toString(),"utf-8");

            httppost.setEntity(jsonEntity);

            CloseableHttpResponse response = httpclient.execute(httppost);

            int httpStatusCode = response.getStatusLine().getStatusCode();

            httpResultDTO.setStatusCode(httpStatusCode);

            responseResult(httpResultDTO, response);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        return httpResultDTO;

    }



}
