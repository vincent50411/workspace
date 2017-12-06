package com.huntkey.multDimesions.exception;

/**
 * Created by liuwens on 2017/8/9.
 */
public class MultipleDimesionsServiceException extends Exception
{

    private String message;

    public MultipleDimesionsServiceException(String exceptionMessage)
    {
        message = exceptionMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
