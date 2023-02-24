package com.hzh.dts.core.strategy;

import com.hzh.dts.core.strategy.mysql.IdSyncStrategy;
import com.hzh.dts.core.strategy.mysql.PageLimitSyncStrategy;

import java.util.concurrent.ConcurrentHashMap;

public class SyncDataStrategyFactory {
    public static final ConcurrentHashMap<String, SyncDataStrategy> SYNC_STRATEGY_MAP = new ConcurrentHashMap<>();

    static {
        IdSyncStrategy idSyncStrategy = new IdSyncStrategy();
        PageLimitSyncStrategy pageLimitSyncStrategy = new PageLimitSyncStrategy();
        SYNC_STRATEGY_MAP.put("id", idSyncStrategy);
        SYNC_STRATEGY_MAP.put("page", pageLimitSyncStrategy);
    }

    public static SyncDataStrategy get(String name) {
        return SYNC_STRATEGY_MAP.get(name);
    }
}
