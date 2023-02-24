package com.hzh.dts.core.strategy.mysql;

import com.hzh.dts.config.SyncConfig;
import com.hzh.dts.core.adapter.OuterAdapter;
import com.hzh.dts.core.strategy.SyncDataStrategy;
import com.hzh.dts.properties.TargetMapping;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;


public class PageLimitSyncStrategy extends MysqlSyncAbstractStrategy {


    @Override
    protected String getSql(long index, SyncConfig syncConfig) {
        return syncConfig.getSql() + " limit " + index + "," + syncConfig.getTargetMapping().getCommitBatch();
    }

    @Override
    protected long getNextIndex(long index, SyncConfig syncConfig, Collection<Map<String, Object>> collection) {
        int commitBatch = syncConfig.getTargetMapping().getCommitBatch();
        return index + commitBatch;
    }
}
