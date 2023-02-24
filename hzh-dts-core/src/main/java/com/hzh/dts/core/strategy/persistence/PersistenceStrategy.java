package com.hzh.dts.core.strategy.persistence;

import com.hzh.dts.core.adapter.OuterAdapter;
import com.hzh.dts.core.strategy.SyncDataStrategy;
import com.hzh.dts.properties.TargetMapping;
import com.hzh.dts.util.JedisUtil;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;


public interface PersistenceStrategy {
     String PREFIX = "dts_persistence:";

    /**
     * 持久性索引
     *
     * @param taskId   任务id
     * @param strategy 策略
     * @param index    指数
     */
    void persistenceIndex(String taskId,String strategy, long index);

    /**
     * 获取持久性索引
     *
     * @param taskId 任务id
     * @return {@link Long}
     */
    Long getPersistenceIndex(String taskId);
}
