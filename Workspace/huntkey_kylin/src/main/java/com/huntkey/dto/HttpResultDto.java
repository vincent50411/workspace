package com.huntkey.dto;

/**
 * Created by liuwens on 2017/8/7.
 */
public class HttpResultDto
{
    private boolean resultStatus;

    private String resultMessage;


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
    }
}
