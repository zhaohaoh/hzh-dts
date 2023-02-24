package com.hzh.dts.core;

import com.hzh.dts.config.SyncConfig;
import com.hzh.dts.core.adapter.OuterAdapter;
import com.hzh.dts.core.adapter.OuterAdapterFactory;
import com.hzh.dts.core.strategy.SyncDataStrategy;
import com.hzh.dts.core.strategy.SyncDataStrategyFactory;
import com.hzh.dts.properties.TargetMapping;
import lombok.extern.slf4j.Slf4j;


/**
 * @Author: hzh
 * @Date: 2023/1/30 19:09
 */
@Slf4j
public class DTSDispatcher {
    private final OuterAdapterFactory outerAdapterFactory;

    public DTSDispatcher(OuterAdapterFactory outerAdapterFactory) {
        this.outerAdapterFactory = outerAdapterFactory;
    }

    public void sync(SyncConfig dtsDTO) {
        if (dtsDTO.getTaskId() == null) {
            throw new RuntimeException("任务id不能为空");
        }
        long start = System.currentTimeMillis();
        OuterAdapter source = outerAdapterFactory.getSource(dtsDTO);
        OuterAdapter target = outerAdapterFactory.getTarget(dtsDTO);
        SyncDataStrategy syncDataStrategy = SyncDataStrategyFactory.get(dtsDTO.getStrategy());
        syncDataStrategy.invoke(dtsDTO, source, target);
        long end = System.currentTimeMillis();
        log.info("全量数据批量处理耗时：{}秒", (end - start) / 1000);
    }

}
