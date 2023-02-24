package com.hzh.dts.properties;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class TargetMapping {
    private Map<String, String> targetPk = new LinkedHashMap<>(); // 目标表主键字段
    private String targetTable;                             // 目标表名
    private Map<String, String> targetColumns;                           // 目标表字段映射

    private String etlCondition;                            // etl条件sql

    private int commitBatch = 5000;                  // etl等批量提交大小

    //下划线转驼峰
    private boolean toCamelCase = true;                  // etl等批量提交大小
}
