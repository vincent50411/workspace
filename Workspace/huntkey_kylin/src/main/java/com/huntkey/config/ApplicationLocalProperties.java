package com.huntkey.config;

import com.huntkey.dto.KylinServerInfoDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by liuwens on 2017/8/7.
 */
@Component
@ConfigurationProperties(prefix = "config", ignoreUnknownFields = false)
public class ApplicationLocalProperties
{


    private KylinServerInfoDto kylin;

    public KylinServerInfoDto getKylin() {
        return kylin;
    }

    public void setKylin(KylinServerInfoDto kylin) {
        this.kylin = kylin;
    }
}
