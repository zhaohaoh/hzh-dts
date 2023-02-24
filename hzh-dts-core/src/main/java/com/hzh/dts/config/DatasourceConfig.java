package com.hzh.dts.config;

import com.hzh.dts.constant.ResourceTypeEnum;
import lombok.Data;


/**
 * 数据源配置
 */
@Data
public class DatasourceConfig implements ResourceConfig {

    private String driver = "com.mysql.cj.jdbc.Driver";   // 默认为mysql jdbc驱动
    private String url;                                      // jdbc url
    private String type = "mysql";                   // 类型, 默认为mysql
    private String username;                                 // jdbc username
    private String password;                                 // jdbc password
    private Integer maxActive = 10;                         // 连接池最大连接数,默认为10

    @Override
    public ResourceTypeEnum getType() {
        return ResourceTypeEnum.DB;
    }
}
