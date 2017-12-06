package com.huntkey.multDimesions.enums;

/**
 * 维度表列枚举
 * Created by liuwens on 2017/8/14.
 */
public enum DimesionTableColumnEnums
{
    PK_COLUMN("_PK", "主键后缀"),
    DIM_COLUMN("DIM_", "维度表中维度列的前缀");

    private String code;

    private String message;

    DimesionTableColumnEnums(String code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code=code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message=message;
    }


}
