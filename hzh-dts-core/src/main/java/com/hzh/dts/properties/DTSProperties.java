package com.hzh.dts.properties;

import cn.hutool.db.Db;
import com.hzh.dts.config.DatasourceConfig;
import com.hzh.dts.config.EsConfig;
import com.hzh.dts.config.ResourceConfig;
import com.hzh.dts.config.SyncConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@ConfigurationProperties("dts")
public class DTSProperties {
    //数据源定义
    private Map<String, DatasourceConfig> dbConfig;
    //数据源定义
    private Map<String, EsConfig> esConfig;

    //同步配置
    private Map<String, SyncConfig> syncConfigs;
}
