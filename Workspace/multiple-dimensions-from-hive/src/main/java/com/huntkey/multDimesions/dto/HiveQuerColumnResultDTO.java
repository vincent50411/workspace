package com.huntkey.multDimesions.dto;

/**
 * kylin cube查询结果 一行中列的信息
 * Created by liuwens on 2017/8/11.
 */
public class HiveQuerColumnResultDTO
{
    private int displaySize;

    private String columnName;

    private Object columnValue;

    //VARCHAR
    private String columnTypeName;

    private String tableName;

    //精度
    private int precision;


    public int getDisplaySize() {
        return displaySize;
    }

    public void setDisplaySize(int displaySize) {
        this.displaySize=displaySize;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName=columnName;
    }

    public Object getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(Object columnValue) {
        this.columnValue=columnValue;
    }

    public String getColumnTypeName() {
        return columnTypeName;
    }

    public void setColumnTypeName(String columnTypeName) {
        this.columnTypeName=columnTypeName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName=tableName;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision=precision;
    }
}
