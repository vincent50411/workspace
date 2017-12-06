package com.huntkey.vo;

import java.util.List;

/**
 * Created by liuwens on 2017/7/3.
 */
public class ORMDataBean
{
    private String tableName;

    private List<?> rowList;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<?> getRowList() {
        return rowList;
    }

    public void setRowList(List<?> rowList) {
        this.rowList = rowList;
    }
}
