package com.huntkey.multDimesions.enums;

/**
 * Created by liuwens on 2017/8/15.
 */
public enum EDMJSONKeyEnums
{
    EDMC_NAME_EN("edmcNameEn", "edm监管类英文名称"),
    EDPG_EDMP_ID("edpgEdmpId", "edm监管类属性ID"),
    FORMULA("formula", "edm监管类卷积公式"),
    TABLE_NAME("tableName", "类对应的表名称");

    private String code;

    private String message;

    EDMJSONKeyEnums(String code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
