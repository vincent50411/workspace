package com.hunkey.dto;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by liuwens on 2017/9/25.
 */
public class KPIRequestParamDto
{
    //资源类编码
    @JSONField(name = "zy_class_code")
    private String zyClassCode;

    @JSONField(name = "zy_attribute_code")
    //资源属性编码
    private String zyAttributeCode;

    @JSONField(name = "monitor_class_code")
    //监管类编码
    private String monitorClassCode;

    @JSONField(name = "monitor_object_code")
    //监管对象编码
    private String monitorObjectCode;

    @JSONField(name = "monitor_attrubite_code")
    //监管属性编码
    private String monitorAttrubiteCode;

    @JSONField(name = "period_code")
    //周期编码
    private String periodCode;

    @JSONField(name = "valid_time")
    //有效时间
    private String validTime;

    @JSONField(name = "convolution_type")
    //卷积类型
    private String convolutionType;


    public String getZyClassCode() {
        return zyClassCode;
    }

    public void setZyClassCode(String zyClassCode) {
        this.zyClassCode=zyClassCode;
    }

    public String getZyAttributeCode() {
        return zyAttributeCode;
    }

    public void setZyAttributeCode(String zyAttributeCode) {
        this.zyAttributeCode=zyAttributeCode;
    }

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

    public String getMonitorAttrubiteCode() {
        return monitorAttrubiteCode;
    }

    public void setMonitorAttrubiteCode(String monitorAttrubiteCode) {
        this.monitorAttrubiteCode=monitorAttrubiteCode;
    }

    public String getPeriodCode() {
        return periodCode;
    }

    public void setPeriodCode(String periodCode) {
        this.periodCode=periodCode;
    }

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime=validTime;
    }

    public String getConvolutionType() {
        return convolutionType;
    }

    public void setConvolutionType(String convolutionType) {
        this.convolutionType=convolutionType;
    }
}
