package com.huntkey.dto;

/**
 * Created by liuwens on 2017/8/7.
 */
public class KylinServerInfoDto
{
    private String server;

    private String user;

    private String password;

    private String contetType;


    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContetType() {
        return contetType;
    }

    public void setContetType(String contetType) {
        this.contetType = contetType;
    }
}
