package com.hzh.dts.config;

import com.hzh.dts.properties.TargetMapping;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.util.Map;


@Data
public class SyncConfig {
    /*
   任务id用于持久化
   */
    private String taskId;
    /*
    sql
    */
    private String sql;
    /*
       前缀是类型数据源key
    */
    private String sourceDataSource;
    /*
      前缀是类型目标数据源key
    */
    private String targetDataSource;
    /*
     映射
    */
    private TargetMapping targetMapping;
    /*
    策略
   */
    private String strategy = "id";
    /*
     每执行多少次保存后持久化数据
   */
    private int persistenceBatch = 10;
}
