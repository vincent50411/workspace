package com.huntkey.multDimesions.service.businessService.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huntkey.multDimesions.config.ApplicationLocalProperties;
import com.huntkey.multDimesions.config.EDMRequestParamDTO;
import com.huntkey.multDimesions.config.ORMRequestParamDTO;
import com.huntkey.multDimesions.dto.*;
import com.huntkey.multDimesions.enums.*;
import com.huntkey.multDimesions.exception.MultipleDimesionsServiceException;
import com.huntkey.multDimesions.service.businessService.MultipleDimensionsManagerService;
import com.huntkey.multDimesions.service.hive.HiveService;
import com.huntkey.multDimesions.service.http.HttpRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.*;

/**
 * 多维度交叉查询算法kylin服务层实现，主要实现对kylin 项目、数据模型和cube的创建以及cube的构建
 * Created by liuwens on 2017/8/7.
 */
@Service(value = "MultipleDimensionsManagerServiceImpl")
public class MultipleDimensionsManagerServiceImpl implements MultipleDimensionsManagerService
{
    private static Logger LOGGER = LoggerFactory.getLogger(MultipleDimensionsManagerServiceImpl.class);

    @Autowired
    @Qualifier(value = "ApplicationLocalProperties")
    private ApplicationLocalProperties applicationLocalProperties;

    @Autowired
    @Qualifier(value ="EDMRequestServiceImpl")
    private HttpRequestService edmRequestService;

    @Autowired
    @Qualifier(value ="ORMRequestServiceImpl")
    private HttpRequestService ormRequestServiceImpl;

    @Autowired
    private HiveService hiveService;

    @Autowired
    private EDMRequestParamDTO edmRequestParamDTO;

    @Autowired
    private ORMRequestParamDTO ormRequestParamDTO;


    /**
     * 查询EDM的监管树服务接口
     * @param sourceClassCode
     * @param attributeID
     * @return
     */
    public HttpResultDTO queryMonitorTreeFromEDMService(String sourceClassCode, String attributeID)
    {
        //覆盖项目路径
        //kylinRequestParamDto.setServerURL("http://192.168.113.22:7002/v1/edmPropertyGroup/monitor/info");
        edmRequestParamDTO.setRestAPI("/v1/edmPropertyGroup/monitor/info");

        JSONObject jsonParam = new JSONObject();

        jsonParam.put("edpg_edmc_id", sourceClassCode);
        jsonParam.put("edpg_edmp_id", attributeID);

        edmRequestParamDTO.setInputJSONParam(jsonParam.toJSONString());

        LOGGER.info("--> 查询EDM的监管树服务请求:" + edmRequestParamDTO.toString());

        HttpResultDTO httpResultDto = edmRequestService.httpPost(edmRequestParamDTO);

        if(httpResultDto.isResultStatus())
        {
            String responseContent = httpResultDto.getResultMessage();

            JSONObject resObj = JSONObject.parseObject(responseContent);

            //数据列表
            JSONArray dataList = (JSONArray)resObj.get("data");

            List<EDMMonitorTreeClassDTO> edmMonitorTreeClassDTOList = new ArrayList<>();

            if(dataList != null)
            {
                for(Object dataItem : dataList)
                {
                    JSONObject dataItemJSONObject = (JSONObject) dataItem;

                    EDMMonitorTreeClassDTO edmMonitorTreeClassDTO = new EDMMonitorTreeClassDTO();
                    edmMonitorTreeClassDTO.setEdmcNameEn((String)dataItemJSONObject.get(EDMJSONKeyEnums.EDMC_NAME_EN.getCode()));
                    edmMonitorTreeClassDTO.setTableName((String)dataItemJSONObject.get(EDMJSONKeyEnums.TABLE_NAME.getCode()));

                    //edm监管类属性ID
                    edmMonitorTreeClassDTO.setEdpgEdmpId((String)dataItemJSONObject.get(EDMJSONKeyEnums.EDPG_EDMP_ID.getCode()));

                    //卷积公式代码, 如果卷积公式为null，则表示该监管类不需要做卷积计算
                    String formulaValue = dataItemJSONObject.get(EDMJSONKeyEnums.FORMULA.getCode()) == null ? String.valueOf(FormulaEnums.NON_FORMULA.getCode()) : (String) dataItemJSONObject.get(EDMJSONKeyEnums.FORMULA.getCode());
                    edmMonitorTreeClassDTO.setFormula(Integer.valueOf(formulaValue));

                    edmMonitorTreeClassDTOList.add(edmMonitorTreeClassDTO);
                }

                httpResultDto.setResultData(edmMonitorTreeClassDTOList);
            }
            else
            {
                LOGGER.info("--> 根据资源类[" + sourceClassCode + "]和属性ID[" + attributeID + "]，没有查询到任何对应监管树信息");
                httpResultDto.setResultStatus(false);
                httpResultDto.setResultData(null);
            }

        }
        else
        {
            httpResultDto.setResultData(null);
        }

        return httpResultDto;
    }

    /**
     * 查询Hive中表的结构信息，主要是列的信息:列名称、列的数据类型
     * @param tableName
     * @param columnFilter
     * @return
     * @throws MultipleDimesionsServiceException
     */
    public  List<TableColumnInfoDTO> queryHiveTabelInfoService(String tableName, String[] columnFilter) throws MultipleDimesionsServiceException
    {
        List<TableColumnInfoDTO> tableColumnList = new ArrayList<>();

        //查询表结构
        String sql = "desc " + tableName;

        LOGGER.info("--> Start Query Hive Table Desc : " + sql);

        ResultSet resultSet = null;

        Connection hiveConnection = null;

        Statement stmt = null;

        try
        {
            hiveConnection = hiveService.getHiveConnection();

            stmt = hiveConnection.createStatement();

            resultSet = stmt.executeQuery(sql);

            while (resultSet.next())
            {
                TableColumnInfoDTO tableColumnInfoDTO = new TableColumnInfoDTO();

                //Hive表元数据信息，列名称col_name 列数据类型data_type 列注释comment
                String columnName = resultSet.getString("col_name") == null ? "" : resultSet.getString("col_name").toUpperCase();

                //有过滤条件，则取特定的列
                for(String filter : columnFilter)
                {
                    if(columnName.indexOf(filter.toUpperCase()) > -1)
                    {
                        tableColumnInfoDTO.setColumnName(columnName);
                        tableColumnInfoDTO.setDataType(resultSet.getString("data_type"));

                        tableColumnList.add(tableColumnInfoDTO);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();

            throw new MultipleDimesionsServiceException("--> Exception:查询Hive表结果异常, " + ex.getMessage());
        }
        finally
        {
            hiveService.closeHive(hiveConnection, stmt, resultSet);
        }

        return tableColumnList;
    }

    /**
     * 根据拼装的SQL，查询Hive，根据提供的返回列名称列表，封装结果集
     * @param sql
     * @return
     */
    public HttpResultDTO queryHiveTableDataService(String sql)
    {
        HttpResultDTO httpResultDto = new HttpResultDTO();

        ResultSet resultSet = null;

        Connection hiveConnection = null;

        Statement stmt = null;

        try
        {
            hiveConnection = hiveService.getHiveConnection();

            stmt = hiveConnection.createStatement();

            //查询记录结果集
            resultSet = stmt.executeQuery(sql);

            //Hive表元数据集
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            //表的列数
            int columnNum = resultSetMetaData.getColumnCount();

            List<HiveQueryRowResultDTO> hiveQueryRowResultDTOList= new ArrayList<>();

            while (resultSet.next())
            {
                HiveQueryRowResultDTO hiveQueryRowResultDTO= new HiveQueryRowResultDTO();
                List<HiveQuerColumnResultDTO> hiveQuerColumnResultDTOList= new ArrayList<>();

                //循环一行结果，根据提供的列名称，获取值
                for(int columnIndex = 1; columnIndex < columnNum; columnIndex++)
                {
                    HiveQuerColumnResultDTO hiveQuerColumnResultDTO= new HiveQuerColumnResultDTO();

                    //列的名称
                    String columnName = resultSetMetaData.getColumnName(columnIndex) == null ? "" : resultSetMetaData.getColumnName(columnIndex).toUpperCase();

                    //列的数据类型
                    String columnDataType = resultSetMetaData.getColumnTypeName(columnIndex) == null ? "" : resultSetMetaData.getColumnTypeName(columnIndex).toLowerCase();

                    int displaySize = resultSetMetaData.getColumnDisplaySize(columnIndex);

                    Object columnValue = resultSet.getObject(columnName);

                    hiveQuerColumnResultDTO.setColumnName(columnName);
                    hiveQuerColumnResultDTO.setColumnTypeName(columnDataType);
                    hiveQuerColumnResultDTO.setColumnValue(columnValue);
                    hiveQuerColumnResultDTO.setDisplaySize(displaySize);

                    hiveQuerColumnResultDTOList.add(hiveQuerColumnResultDTO);
                }

                //保存一行记录中所有列的信息
                hiveQueryRowResultDTO.setColumns(hiveQuerColumnResultDTOList);

                hiveQueryRowResultDTOList.add(hiveQueryRowResultDTO);
            }

            httpResultDto.setResultStatus(true);
            httpResultDto.setResultData(hiveQueryRowResultDTOList);
        }
        catch (Exception ex)
        {
            httpResultDto.setResultStatus(false);
            ex.printStackTrace();
        }
        finally
        {
            hiveService.closeHive(hiveConnection, stmt, resultSet);
        }

        return httpResultDto;
    }



    /**
     * 保存cube查询结果, 本期累计: 需要根据监管树类的计算公式判断
     * @param edmName EDM中监管统计类的名称
     * @param monitorClassName
     * @param monitorClassAttributeID 是监管类属性ID，不是资源类属性ID
     * @param formulaCode 卷积公式code
     * @param hiveQueryRowResultDTOList
     */
    public void saveHiveQueryResultByORMService(String edmName, String monitorClassName, String monitorClassAttributeID, int formulaCode, List<HiveQueryRowResultDTO> hiveQueryRowResultDTOList)
    {
        JSONObject ormAddParam = new JSONObject();
        ormAddParam.put("edmName", edmName);

        JSONArray recordParamList = new JSONArray();

        //行记录
        for(HiveQueryRowResultDTO hiveQueryRowResultDTO : hiveQueryRowResultDTOList)
        {
            List<HiveQuerColumnResultDTO> columns = hiveQueryRowResultDTO.getColumns();

            JSONObject recordItemParam = new JSONObject();

            //所属监管类
            recordItemParam.put("stat001", monitorClassName);

            //所属监管类对象, 需要按照维度列规则匹配结果集, 包含DIM_
            HiveQuerColumnResultDTO dimColumnDTO = findKylinQuerColumnResultDTO(columns, DimesionTableColumnEnums.DIM_COLUMN.getCode());
            if(dimColumnDTO != null)
            {
                recordItemParam.put("stat002", dimColumnDTO.getColumnValue());
            }

            //监管树属性ID
            recordItemParam.put("stat003", monitorClassAttributeID);

            //周期名: 周期表的主键
            HiveQuerColumnResultDTO cycleTimeColumnDTO = findKylinQuerColumnResultDTO(columns, FactTableColumnEnums.CYCLE_TIME_ID_COLUMN.getCode());
            if(cycleTimeColumnDTO != null)
            {
                recordItemParam.put("stat006", cycleTimeColumnDTO.getColumnValue());
            }

            //期初余额
            recordItemParam.put("stat010", 0);

            //本期累计: 需要根据监管树类的计算公式判断， SUM_VALUE
            HiveQuerColumnResultDTO sumValueColumnDTO;
            if(formulaCode == FormulaEnums.COUNT_FORMULA.getCode())
            {
                sumValueColumnDTO = findKylinQuerColumnResultDTO(columns, MeasuresEnums.COUNT_VALUE.getCode());
            }
            else if(formulaCode == FormulaEnums.AVG_FORMULA.getCode())
            {
                sumValueColumnDTO = findKylinQuerColumnResultDTO(columns, MeasuresEnums.AVG_VALUE.getCode());
            }
            else
            {
                //默认取累计求和公式
                sumValueColumnDTO = findKylinQuerColumnResultDTO(columns, MeasuresEnums.SUM_VALUE.getCode());
            }

            if(cycleTimeColumnDTO != null)
            {
                recordItemParam.put("stat011", sumValueColumnDTO.getColumnValue());

                //本期余额
                recordItemParam.put("stat012", sumValueColumnDTO.getColumnValue());
            }

            recordParamList.add(recordItemParam);
        }

        ormAddParam.put("params", recordParamList);

        LOGGER.info("--> 往HBase中的统计类[" + edmName + "]中，写入[" + recordParamList.size() + "]条记录");

        //post请求需要的参数
        ormRequestParamDTO.setRestAPI("/servicecenter/add");
        ormRequestParamDTO.setInputJSONParam(ormAddParam.toJSONString());

        //LOGGER.info("--> 保存cube结果到HBase [" + edmName + "] 表请求:" + kylinRequestParamDto.toString());

        //调用ORM服务，保存数据
        ormRequestServiceImpl.httpPost(ormRequestParamDTO);

    }


    /**
     * 根据列的名称从结果集中过滤特定的列信息
     * @param columns
     * @param filter
     * @return
     */
    private HiveQuerColumnResultDTO findKylinQuerColumnResultDTO(List<HiveQuerColumnResultDTO> columns, String filter)
    {
        for(HiveQuerColumnResultDTO hiveQuerColumnResultDTO : columns)
        {
            String resultColumnName = hiveQuerColumnResultDTO.getColumnName().toUpperCase();

            if(resultColumnName.indexOf(filter.toUpperCase()) > -1)
            {
                return hiveQuerColumnResultDTO;
            }
        }

        return null;
    }





}
