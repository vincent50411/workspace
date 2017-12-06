package com.huntkey.service;

import com.huntkey.vo.ORMDataBean;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;

/**
 * Created by liuwens on 2017/7/4.
 */
@Service
public class RadomDataService
{

    public ORMDataBean createRadomDataService(String tableName, String pkColumnName, String labelName, int size)
    {
        ORMDataBean ormDataBean = new ORMDataBean();

        ormDataBean.setTableName(tableName);

        List rowList = new ArrayList();

        int rowIndex = 0;
        int columnNum = 3;

        while(rowIndex < size)
        {
            Map column = new HashMap();

            for (int columnIndex = 0; columnIndex < columnNum; columnIndex++)
            {
                // 获得指定列的列名
                String columnName = "col_label_" + columnIndex;

                // 获得指定列的列值
                String columnValue = "col_value_" + rowIndex + "_" + columnIndex;

                if(columnIndex == 0)
                {
                    //主键
                    columnName = pkColumnName;
                    columnValue = UUID.randomUUID().toString();
                }

                if(columnIndex == 1)
                {
                    //别名
                    columnName = "COL_LABLE";
                    columnValue = labelName + "_" + rowIndex;
                }

                column.put(columnName, columnValue);
            }

            rowList.add(column);

            rowIndex ++;
        }

        ormDataBean.setRowList(rowList);

        return ormDataBean;

    }


    /**
     * 随机抽取一行数据的主键值
     * @param ormDataBean
     * @param pkColumnName
     * @return
     */
    public String radomDimesionDataService(ORMDataBean ormDataBean, String pkColumnName)
    {
        List row = ormDataBean.getRowList();

        //随机抽取列表数据组合主表数据
        int rowSize = row.size();

        SecureRandom random = new SecureRandom();
        int index = random.nextInt(rowSize);

        Map column = (Map)row.get(index);

        //第一位是主键
        String keyValue = (String)column.get(pkColumnName);

        return keyValue;
    }




}
