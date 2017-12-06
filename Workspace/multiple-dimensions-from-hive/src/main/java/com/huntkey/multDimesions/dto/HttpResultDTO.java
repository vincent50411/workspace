package com.huntkey.multDimesions.dto;

/**
 * Created by liuwens on 2017/8/7.
 */
public class HttpResultDTO
{
    //结果状态
    private boolean resultStatus = false;

    //结果消息，错误消息
    private String resultMessage = null;

    //携带的数据
    private Object resultData = null;

    public Object getResultData() {
        return resultData;
    }

    public void setResultData(Object resultData) {
        this.resultData = resultData;
    }

    public boolean isResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(boolean resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;

        if(isResultStatus())
        {
            //如果执行成功，默认将response返回的消息设置为数据结果
            this.resultData = resultMessage;
        }
    }
}
