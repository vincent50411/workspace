package com.huntkey.multDimesions.controller;

import com.huntkey.multDimesions.common.Contants;
import com.huntkey.multDimesions.comparator.KylinDimesionTableColumnComparator;
import com.huntkey.multDimesions.dto.*;
import com.huntkey.multDimesions.enums.DimesionTableColumnEnums;
import com.huntkey.multDimesions.enums.FactTableColumnEnums;
import com.huntkey.multDimesions.enums.FormulaEnums;
import com.huntkey.multDimesions.enums.MeasuresEnums;
import com.huntkey.multDimesions.exception.MultipleDimesionsServiceException;
import com.huntkey.multDimesions.service.businessService.MultipleDimensionsManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by liuwens on 2017/8/21.
 */
@Component
public class MultipleDimensionsManagerController
{
    private static Logger LOGGER = LoggerFactory.getLogger(MultipleDimensionsManagerController.class);

    @Autowired
    private MultipleDimensionsManagerService multipleDimensionsManagerService;



    public void init(String resourceClassCode, String attributeID)
    {
        LOGGER.info("--> 查询EDM监管树");

        try {
            // 1、查询监管树
            List<EDMMonitorTreeClassDTO> edmMonitorTreeClassDTOList = queryMonitorTreeFromEDM(resourceClassCode, attributeID);

            if (edmMonitorTreeClassDTOList == null || edmMonitorTreeClassDTOList.size() == 0) {
                LOGGER.info("--> 没有监管树需要进行多监管维度交叉计算");
                return;
            }

            //因为同一资源对应多个子监管树
            for(EDMMonitorTreeClassDTO edmMonitorTreeClassDTO : edmMonitorTreeClassDTOList)
            {
                //监管树类名称
                String monitorClassName = edmMonitorTreeClassDTO.getEdmcNameEn();

                //监管树类属性ID
                String monitorClassAttributeID = edmMonitorTreeClassDTO.getEdpgEdmpId();

                //监管树卷积公式, 如果卷积公式不存在，则不做卷积计算
                int monitorClassConvolutionFormula = edmMonitorTreeClassDTO.getFormula();

                if (monitorClassConvolutionFormula == FormulaEnums.NON_FORMULA.getCode())
                {
                    //如果该监管类的卷积公式不存在，则不需要做卷积计算
                    LOGGER.info("--> 监管类[" + monitorClassName + "]的卷积公式不存在，则不需要做卷积计算");
                    break;
                }

                //事实表名称
                String factTableName = Contants.FACT_TABLE_PREFIX + resourceClassCode + "_" + attributeID;

                //维度表名称
                String dimesionTableName = Contants.DIMESION_TABLE_PREFIX + resourceClassCode + "_" + monitorClassName;


                //查询Hive数据仓库，组装卷积查询sql
                //查询cube时需要的参数
                HiveParamFroSingleDimesionDTO hiveParamFroSingleDimesionDTO= new HiveParamFroSingleDimesionDTO();

                hiveParamFroSingleDimesionDTO.setResourceClassCode(resourceClassCode);
                hiveParamFroSingleDimesionDTO.setMonitorClassName(monitorClassName);
                hiveParamFroSingleDimesionDTO.setMonitorClassAttributeID(monitorClassAttributeID);
                hiveParamFroSingleDimesionDTO.setFactTableName(factTableName);
                hiveParamFroSingleDimesionDTO.setDimesionTableName(dimesionTableName);
                hiveParamFroSingleDimesionDTO.setMeasureColumnName(attributeID);

                //设置卷积公式
                hiveParamFroSingleDimesionDTO.setMonitorClassConvolutionFormula(monitorClassConvolutionFormula);

                queryCubeAndSaveForSingleDimesion(hiveParamFroSingleDimesionDTO);


            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    /**
     * 根据资源类编码和资源属性ID，通过调用EDM返回资源类对应的监管树所有监管子树
     * @param sourceClassCode 资源类编码
     * @param attributeID     资源属性ID
     * @return
     */
    private List<EDMMonitorTreeClassDTO> queryMonitorTreeFromEDM(String sourceClassCode, String attributeID)
    {
        List<EDMMonitorTreeClassDTO> edmMonitorTreeClassDTOList;

        //查询EDM服务，返回资源监管树对应的所有监管类列表
        HttpResultDTO httpResultDto = multipleDimensionsManagerService.queryMonitorTreeFromEDMService(sourceClassCode, attributeID);

        if(httpResultDto.isResultStatus())
        {
            edmMonitorTreeClassDTOList = (List<EDMMonitorTreeClassDTO>) httpResultDto.getResultData();
        }
        else
        {
            edmMonitorTreeClassDTOList = null;
        }

        return edmMonitorTreeClassDTOList;
    }

    /**
     * 根据条件动态拼装查询Hive的SQL，根据维度表的维度层级设置group by卷积条件
     * @param hiveParamFroSingleDimesionDTO
     * @throws MultipleDimesionsServiceException
     */
    public void queryCubeAndSaveForSingleDimesion(HiveParamFroSingleDimesionDTO hiveParamFroSingleDimesionDTO) throws MultipleDimesionsServiceException
    {
        String resourceClassCode = hiveParamFroSingleDimesionDTO.getResourceClassCode();

        //监管树类名称
        String monitorClassName = hiveParamFroSingleDimesionDTO.getMonitorClassName();
        String factTableName = hiveParamFroSingleDimesionDTO.getFactTableName();
        String dimesionTableName = hiveParamFroSingleDimesionDTO.getDimesionTableName();

        //资源类属性ID
        String measureColumnName = hiveParamFroSingleDimesionDTO.getMeasureColumnName();

        //监管类属性ID
        String monitorClassAttributeID = hiveParamFroSingleDimesionDTO.getMonitorClassAttributeID();

        //卷积公式code
        int formulaCode = hiveParamFroSingleDimesionDTO.getMonitorClassConvolutionFormula();

        //事实表分组查询条件:周期维度ID
        String factDimesionColumnName = factTableName + "." + FactTableColumnEnums.CYCLE_TIME_ID_COLUMN + ", ";


        String[] dimesionTableFilter = {DimesionTableColumnEnums.DIM_COLUMN.getCode()};
        //组合group by条件，根据维度表中DIM维度列个数，level从0->... DIM_资源类_levelIndex 0表示最高级别
        List<TableColumnInfoDTO> dimesionTableColumnInfoDTOList = multipleDimensionsManagerService.queryHiveTabelInfoService(dimesionTableName, dimesionTableFilter);

        //维度表列名称比较器
        KylinDimesionTableColumnComparator columnComparator = new KylinDimesionTableColumnComparator();

        //对维度列进行自然排序，作为分组模式的列组合 由大向小 下钻
        Collections.sort(dimesionTableColumnInfoDTOList, columnComparator);

        String dimesionLevelColumn = " ";

        String dimesionWhereColumn = "";

        //多次循环查询和保存数据
        for(TableColumnInfoDTO tableColumnInfoDTO : dimesionTableColumnInfoDTOList)
        {
            //当前的维度层级列
            String dimesionColumnName = dimesionTableName + "." + tableColumnInfoDTO.getColumnName();

            //当前维度层级列的类型
            String dimesionColumnType = tableColumnInfoDTO.getDataType() == null ? "" : tableColumnInfoDTO.getDataType().toUpperCase();

            //层级列
            dimesionLevelColumn += dimesionColumnName + ",";

            //拼group by 语句
            String groupBy = dimesionLevelColumn.substring(0, dimesionLevelColumn.length() - 1);

            //拼where语句
            dimesionWhereColumn += dimesionColumnName + " IS NOT NULL AND ";

            String where = dimesionWhereColumn.substring(0, dimesionWhereColumn.lastIndexOf("AND "));

            //事实表中关联维度的外键
            String factTableFK = resourceClassCode + FactTableColumnEnums.FK_COLUMN.getCode();

            ///维度表中的主键
            String dimesionTablePK = resourceClassCode + DimesionTableColumnEnums.PK_COLUMN.getCode();

            //查询Hive的SQL, 返回结果集中主要包括事实表涉及的列、当前维度层级列、所有的聚集函数，计数、求和、求平均值
            String sql = "select " + factDimesionColumnName +
                    dimesionColumnName +
                    ", COUNT(1) as " + MeasuresEnums.COUNT_VALUE.getCode() +
                    ", SUM(" + factTableName + "." + measureColumnName + ") as " + MeasuresEnums.SUM_VALUE.getCode() +
                    ", SUM(" + factTableName + "." + measureColumnName + ")/COUNT(1) as " + MeasuresEnums.AVG_VALUE.getCode() +
                    " from " + factTableName + " as " + factTableName +
                    " inner join " + dimesionTableName + " as " + dimesionTableName + " on " + factTableName + "." + factTableFK + " = " + dimesionTableName + "." + dimesionTablePK +
                    " WHERE " + where +
                    " group by " + factDimesionColumnName + groupBy;

            //去除查询SQL中表的前缀DEFAULT.
            sql = sql.replace("DEFAULT.", "");

            //sql返回结果列名称列表
            List<TableColumnInfoDTO> tableColumnNameList = new ArrayList<>();
            //目前事实表涉及的维度列主要是周期ID
            tableColumnNameList.add(new TableColumnInfoDTO(factDimesionColumnName, "string"));

            //维度表，当前维度层级列
            tableColumnNameList.add(new TableColumnInfoDTO(dimesionColumnName, dimesionColumnType));

            //计数函数
            tableColumnNameList.add(new TableColumnInfoDTO(MeasuresEnums.COUNT_VALUE.getCode(), "bigint"));

            //求和, 数据类型主要取决事实表中的measure列类型
            tableColumnNameList.add(new TableColumnInfoDTO(MeasuresEnums.SUM_VALUE.getCode(), "dobule"));

            //求平均值
            tableColumnNameList.add(new TableColumnInfoDTO(MeasuresEnums.AVG_VALUE.getCode(), "dobule"));

            LOGGER.info("--> SQL:" + sql);

            //发起查询服务请求，需要设计分批结果处理
            HttpResultDTO cubeBuildResult = multipleDimensionsManagerService.queryHiveTableDataService(sql);

            if(cubeBuildResult.isResultStatus())
            {
                //返回查询结果
                List<HiveQueryRowResultDTO> hiveQueryRowResultDTOList= (List)cubeBuildResult.getResultData();

                //将查询结果保存到HBase表中, 本期累计: 需要根据监管树类的计算公式判断
                multipleDimensionsManagerService.saveHiveQueryResultByORMService(Contants.STATISTICS, monitorClassName, monitorClassAttributeID, formulaCode, hiveQueryRowResultDTOList);
            }
        }
    }




}
