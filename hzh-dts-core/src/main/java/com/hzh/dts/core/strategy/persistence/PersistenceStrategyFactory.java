package com.hzh.dts.core.strategy.persistence;

import com.hzh.dts.core.strategy.SyncDataStrategy;
import com.hzh.dts.core.strategy.mysql.IdSyncStrategy;
import com.hzh.dts.core.strategy.mysql.PageLimitSyncStrategy;

import java.util.concurrent.ConcurrentHashMap;

public class PersistenceStrategyFactory {
    public static final ConcurrentHashMap<String, PersistenceStrategy> SYNC_STRATEGY_MAP = new ConcurrentHashMap<>();

    static {
        RedisPersistenceStrategy redisPersistenceStrategy = new RedisPersistenceStrategy();
        SYNC_STRATEGY_MAP.put("redis", redisPersistenceStrategy);
    }

    public static PersistenceStrategy get(String name) {
        return SYNC_STRATEGY_MAP.get(name);
    }
}
