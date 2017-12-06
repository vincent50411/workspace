package com.huntkey.multDimesions.config;

import com.huntkey.multDimesions.dto.EDMServerInfoDTO;
import com.huntkey.multDimesions.dto.HiveServerInfoDTO;
import com.huntkey.multDimesions.dto.ORMServerInfoDTO;
import com.huntkey.multDimesions.service.hive.HiveService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by liuwens on 2017/8/21.
 */
@Component(value = "ApplicationLocalProperties")
@ConfigurationProperties(prefix = "config", ignoreUnknownFields = false)
public class ApplicationLocalProperties
{
    private EDMServerInfoDTO edm;

    private ORMServerInfoDTO orm;

    private HiveServerInfoDTO hive;


    @Bean
    public HiveService getHiveService()
    {
        HiveService hiveService = new HiveService(hive.getServer(), hive.getHiveDriver(), hive.getUserName(), hive.getPassword());

        return hiveService;
    }


    public EDMServerInfoDTO getEdm() {
        return edm;
    }

    public void setEdm(EDMServerInfoDTO edm) {
        this.edm=edm;
    }

    public ORMServerInfoDTO getOrm() {
        return orm;
    }

    public void setOrm(ORMServerInfoDTO orm) {
        this.orm=orm;
    }

    public HiveServerInfoDTO getHive() {
        return hive;
    }

    public void setHive(HiveServerInfoDTO hive) {
        this.hive=hive;
    }
}
