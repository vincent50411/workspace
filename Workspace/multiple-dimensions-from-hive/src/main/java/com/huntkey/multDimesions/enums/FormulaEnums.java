package com.huntkey.multDimesions.enums;

/**
 * 卷积公式枚举
 * Created by liuwens on 2017/8/17.
 */
public enum FormulaEnums
{
     NON_FORMULA(-1, "不是卷积公式"),
    AVG_FORMULA(1, "计算平均值公式"),
    SUM_FORMULA(2, "累计数值求和公式"),
    AMASS_FORMULA(3, "累计数值求积公式"),
    COUNT_FORMULA(4, "累计计数公式");

    private int code;

    private String message;

    FormulaEnums(int code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code=code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message=message;
    }
}
