package com.huntkey.multDimesions.service.hive;

import java.sql.*;

/**
 * Created by liuwens on 2017/8/21.
 */
public class HiveService
{
    private String hiveURL;

    private String driverClass;

    private String userName;

    private String password;

    public HiveService(String hiveURL, String driverClass, String userName, String password)
    {
        this.hiveURL = hiveURL;

        this.driverClass = driverClass;

        this.userName = userName;

        this.password = password;
    }

    public Connection getHiveConnection()
    {
        Connection hiveConnection = null;

        if (null == hiveConnection)
        {
            synchronized (HiveService.class)
            {
                if (null == hiveConnection)
                {
                    try {
                        //hive的jdbc驱动类, 不能多hadoop这个路径
                        Class.forName(driverClass);

                        //登录linux的用户名  一般会给权限大一点的用户，否则无法进行事务形操作
                        hiveConnection = DriverManager.getConnection(hiveURL, userName, password);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return hiveConnection;

    }

    public void closeHive(Connection hiveConnection, Statement stmt, ResultSet resultSet)
    {
        try
        {
            if(resultSet != null)
            {
                resultSet.close();
            }

            if(stmt != null)
            {
                stmt.close();
            }

            if(hiveConnection != null)
            {
                hiveConnection.close();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }



}
