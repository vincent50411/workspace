package com.huntkey.multDimesions.service.businessService;


import com.huntkey.multDimesions.dto.HttpResultDTO;
import com.huntkey.multDimesions.dto.HiveQueryRowResultDTO;
import com.huntkey.multDimesions.dto.TableColumnInfoDTO;
import com.huntkey.multDimesions.exception.MultipleDimesionsServiceException;

import java.util.List;

/**
 * Created by liuwens on 2017/8/7.
 */
public interface MultipleDimensionsManagerService
{

    /**
     * 查询EDM的监管树服务接口
     * @param sourceClassCode
     * @param attributeID
     * @return
     */
    HttpResultDTO queryMonitorTreeFromEDMService(String sourceClassCode, String attributeID);


    List<TableColumnInfoDTO> queryHiveTabelInfoService(String tableName, String[] columnFilter) throws MultipleDimesionsServiceException;

    HttpResultDTO queryHiveTableDataService(String sql);

    /**
     * 保存卷积计算结果
     * @param edmName EDM中统计类的名称
     * @param monitorClassName 监管类名称
     * @param attributeID 监管属性ID
     * @param formulaCode 卷积公式code
     * @param hiveQueryRowResultDTOList 查询的结果集
     */
    void saveHiveQueryResultByORMService(String edmName, String monitorClassName, String attributeID, int formulaCode, List<HiveQueryRowResultDTO> hiveQueryRowResultDTOList);

}
