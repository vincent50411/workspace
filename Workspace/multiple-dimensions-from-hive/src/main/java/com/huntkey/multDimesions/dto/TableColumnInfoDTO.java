package com.huntkey.multDimesions.dto;

/**
 * Kylin 中 hive表结构列信息 { id: "1",name: "CODE",datatype: "string"}
 * Created by liuwens on 2017/8/10.
 */
public class TableColumnInfoDTO
{
    private int id;

    private String columnName;

    private String dataType;

    public TableColumnInfoDTO()
    {

    }

    public TableColumnInfoDTO(String columnName, String dataType)
    {
        this.columnName = columnName;
        this.dataType = dataType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
