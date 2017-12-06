package com.huntkey.multDimesions.dto;

/**
 * Created by liuwens on 2017/8/21.
 */
public class HiveServerInfoDTO extends BaseServerInfoDTO
{

    private String hiveDriver;

    private String userName;

    private String password;


    public String getHiveDriver() {
        return hiveDriver;
    }

    public void setHiveDriver(String hiveDriver) {
        this.hiveDriver=hiveDriver;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName=userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password=password;
    }
}
