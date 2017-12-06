package com.huntkey.multDimesions.dto;

/**
 * EDM监管树类信息
 * Created by liuwens on 2017/8/15.
 */
public class EDMMonitorTreeClassDTO
{
    //edm英文名称
    private String edmcNameEn;

    //类对应的表名称
    private String tableName;

    //监管类卷积属性ID
    private String edpgEdmpId;

    //卷积公式code
    private int formula;

    public String getEdmcNameEn() {
        return edmcNameEn;
    }

    public void setEdmcNameEn(String edmcNameEn) {
        this.edmcNameEn=edmcNameEn;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName=tableName;
    }

    public String getEdpgEdmpId() {
        return edpgEdmpId;
    }

    public void setEdpgEdmpId(String edpgEdmpId) {
        this.edpgEdmpId=edpgEdmpId;
    }

    public int getFormula() {
        return formula;
    }

    public void setFormula(int formula) {
        this.formula=formula;
    }
}
