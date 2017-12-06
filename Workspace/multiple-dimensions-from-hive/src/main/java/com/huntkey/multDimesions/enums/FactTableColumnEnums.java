package com.huntkey.multDimesions.enums;

/**
 * Created by liuwens on 2017/8/14.
 */
public enum FactTableColumnEnums
{
    FK_COLUMN("_FK", "外键后缀"),
    ISSUE_COLUMN("ISSUE", "期次列名称"),
    CYCLE_TIME_ID_COLUMN("CYCLE_TIME_ID", "周期表关联ID列名称"),
    SERVICE_TIME("SERVICE_TIME", "业务发生的时间");

    private String code;

    private String message;

    FactTableColumnEnums(String code, String message)
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
