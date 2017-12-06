package com.huntkey.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by liuwens on 2017/8/24.
 */
@Configuration
public class MysqlConfig
{
    @Value("${mysqldb.datasource.url}")
    private String url;

    @Value("${mysqldb.datasource.username}")
    private String username;

    @Value("${logging.level.org.springframework.web}")
    private String debug;

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug=debug;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url=url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username=username;
    }
}
