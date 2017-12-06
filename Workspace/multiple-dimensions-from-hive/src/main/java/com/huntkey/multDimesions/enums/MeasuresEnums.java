package com.huntkey.multDimesions.enums;

/**
 * Created by liuwens on 2017/8/9.
 */
public enum MeasuresEnums
{
    COUNT_VALUE("COUNT_VALUE", "统计函数"),
    SUM_VALUE("SUM_VALUE", "求和函数"),
    AVG_VALUE("AVG_VALUE", "平均值值函数"),
    MAX_VALUE("MAX_VALUE", "最大值函数"),
    MIN_VALUE("MIN_VALUE", "最小值函数");

    private String code;

    private String message;

    MeasuresEnums(String code, String message)
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
