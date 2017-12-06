package com.huntkey.multDimesions.dto;

import com.huntkey.multDimesions.enums.FormulaEnums;

/**
 * Cube查询需要的参数, 只支持单维度
 * Created by liuwens on 2017/8/14.
 */
public class HiveParamFroSingleDimesionDTO
{
    private String resourceClassCode;

    //监管树类名称
    private String monitorClassName;

    //监管树类属性ID
    private String monitorClassAttributeID;

    //卷积公式:计数、求和、平均数等;
    private int monitorClassConvolutionFormula;

    //只要监管类属性ID是卷积类型，则一定会有卷积公式；反之，如果属性ID没有卷积公式，则该属性不是卷积类型
    private boolean isConvolutionType;

    private String kylinProjectName;

    private String factTableName;

    private String dimesionTableName;

    private String measureColumnName;

    public String getResourceClassCode() {
        return resourceClassCode;
    }

    public void setResourceClassCode(String resourceClassCode) {
        this.resourceClassCode=resourceClassCode;
    }

    public String getMonitorClassName() {
        return monitorClassName;
    }

    public void setMonitorClassName(String monitorClassName) {
        this.monitorClassName=monitorClassName;
    }

    public String getKylinProjectName() {
        return kylinProjectName;
    }

    public void setKylinProjectName(String kylinProjectName) {
        this.kylinProjectName=kylinProjectName;
    }

    public String getFactTableName() {
        return factTableName;
    }

    public void setFactTableName(String factTableName) {
        this.factTableName=factTableName;
    }

    public String getDimesionTableName() {
        return dimesionTableName;
    }

    public void setDimesionTableName(String dimesionTableName) {
        this.dimesionTableName=dimesionTableName;
    }

    public String getMeasureColumnName() {
        return measureColumnName;
    }

    public void setMeasureColumnName(String measureColumnName) {
        this.measureColumnName=measureColumnName;
    }

    public String getMonitorClassAttributeID() {
        return monitorClassAttributeID;
    }

    public void setMonitorClassAttributeID(String monitorClassAttributeID) {
        this.monitorClassAttributeID=monitorClassAttributeID;
    }

    public int getMonitorClassConvolutionFormula() {
        return monitorClassConvolutionFormula;
    }

    public void setMonitorClassConvolutionFormula(int monitorClassConvolutionFormula) {
        this.monitorClassConvolutionFormula=monitorClassConvolutionFormula;
    }

    public boolean isConvolutionType()
    {
        //只要监管类属性ID是卷积类型，则一定会有卷积公式；反之，如果属性ID没有卷积公式，则该属性不是卷积类型
        if(this.monitorClassConvolutionFormula == FormulaEnums.NON_FORMULA.getCode())
        {
            isConvolutionType = false;
        }
        else
        {
            isConvolutionType = true;
        }

        return isConvolutionType;
    }


}
