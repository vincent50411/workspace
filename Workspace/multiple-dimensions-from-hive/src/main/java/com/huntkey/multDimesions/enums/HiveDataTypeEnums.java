package com.huntkey.multDimesions.enums;

/**
 * kylin使用hive表，基础数据类型参照Hive
 * Created by liuwens on 2017/8/15.
 */
public enum HiveDataTypeEnums
{
    TINYINT("TINYINT", "1个字节（8位）有符号整数"),
    SMALLINT("SMALLINT", "2字节（16位）有符号整数"),
    INT("INT", "4字节（32位）有符号整数"),
    BIGINT("BIGINT", "8字节（64位）有符号整数"),
    DOUBLE("DOUBLE", "8字节（64位）双精度浮点数"),
    FLOAT("FLOAT", "4字节（32位）单精度浮点数"),
    BOOLEAN("BOOLEAN", "true/false"),
    BINARY("BINARY", ""),
    CHAR("CHAR", "字符串类型的数据类型, 255"),
    VARCHAR("VARCHAR", "字符串类型的数据类型， 1 to 65355"),
    STRING("STRING", "String类型相当于varchar类型，该类型是一个可变的字符串，不过它不能声明其中最多能存储多少个字符，理论上它可以存储2GB的字符数"),
    TIMESTAMP("TIMESTAMP", "它支持传统的UNIX时间戳可选纳秒的精度。它支持的java.sql.Timestamp格式“YYYY-MM-DD HH:MM:SS.fffffffff”和格式“YYYY-MM-DD HH:MM:ss.ffffffffff”"),
    DECIMAL("DECIMAL", "用于表示不可改变任意精度"),
    DATE("DATE", "DATE值在年/月/日的格式形式描述 {{YYYY-MM-DD}}.");

    private String code;

    private String message;

    HiveDataTypeEnums(String code, String message)
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
