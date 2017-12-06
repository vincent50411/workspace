package com.example.demo.dto;

/**
 * Created by liuwens on 2017/6/28.
 */
public class HttpResultDTO {

    private int statusCode;

    private String resultMessage;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }
}
