package com.hzh.dts.pojo;

import com.hzh.dts.config.ResourceConfig;
import lombok.Data;

@Data
public class SourceConfig {
    private String sourceType;
    private ResourceConfig datasourceInstance;
}
