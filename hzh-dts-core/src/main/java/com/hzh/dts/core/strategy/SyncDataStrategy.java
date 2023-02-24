package com.hzh.dts.core.strategy;

import com.hzh.dts.config.SyncConfig;
import com.hzh.dts.core.adapter.OuterAdapter;
import com.hzh.dts.properties.TargetMapping;

/**
 * 同步数据策略
 *
 * @author hzh
 * @date 2023/02/21
 */
public interface SyncDataStrategy {
    void invoke(SyncConfig syncConfig, OuterAdapter source, OuterAdapter target);
}
