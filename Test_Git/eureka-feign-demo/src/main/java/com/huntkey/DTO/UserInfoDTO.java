package com.huntkey.DTO;


import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by liuwens on 2017/8/18.
 */
public class UserInfoDTO
{
    @JSONField(name = "user_name")
    private String userName;

    @JSONField(name = "user_age")
    private int userAge;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName=userName;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge=userAge;
    }
}
