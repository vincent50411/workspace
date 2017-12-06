package com.example.demo.dto;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by liuwens on 2017/9/20.
 */
public class RequestParamDTO {

    @JSONField(name = "monitor_class_code")
    private String monitorClassCode;

    @JSONField(name = "monitor_object_code")
    private String monitorObjectCode;

    public String getMonitorClassCode() {
        return monitorClassCode;
    }

    public void setMonitorClassCode(String monitorClassCode) {
        this.monitorClassCode=monitorClassCode;
    }

    public String getMonitorObjectCode() {
        return monitorObjectCode;
    }

    public void setMonitorObjectCode(String monitorObjectCode) {
        this.monitorObjectCode=monitorObjectCode;
    }
}
