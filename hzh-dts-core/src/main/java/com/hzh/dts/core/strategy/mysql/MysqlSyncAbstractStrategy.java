package com.hzh.dts.core.strategy.mysql;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.JdbcConstants;
import com.hzh.dts.config.SyncConfig;
import com.hzh.dts.core.adapter.OuterAdapter;
import com.hzh.dts.core.strategy.SyncDataStrategy;
import com.hzh.dts.core.strategy.persistence.PersistenceStrategy;
import com.hzh.dts.core.strategy.persistence.PersistenceStrategyFactory;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class MysqlSyncAbstractStrategy implements SyncDataStrategy {

    @Override
    public void invoke(SyncConfig syncConfig, OuterAdapter source, OuterAdapter target) {
        //获取持久化的索引
        PersistenceStrategy persistenceAbstractStrategy = PersistenceStrategyFactory.get("redis");
        Long persistenceIndex = persistenceAbstractStrategy.getPersistenceIndex(syncConfig.getTaskId());
        long index = 0;
        if (persistenceIndex != null) {
            index = persistenceIndex;
        }
        String sql;
        Collection<Map<String, Object>> collection;
        long count = 0;
        while (true) {
            //sql格式校验
            List<SQLStatement> sqlStatements = SQLUtils.parseStatements(syncConfig.getSql(), JdbcConstants.MYSQL);
            if (sqlStatements.size() != 1) {
                throw new RuntimeException("sql injection detected : " + syncConfig.getSql());
            }
            //根据不同的策略和查询索引下标获取sql
            sql = getSql(index, syncConfig);

            // 每10次持久化一次执行索引 不同的任务id如果策略变了也会变
            if (count % syncConfig.getPersistenceBatch() == 0) {
                persistenceAbstractStrategy.persistenceIndex(syncConfig.getTaskId(), syncConfig.getStrategy(),index);
            }

            //查询操作和保存
            collection = source.select(sql);
            if (CollectionUtils.isEmpty(collection)) {
                return;
            }
            target.save(syncConfig.getTargetMapping(), collection);

            //获取下一个索引用于遍历
            index = getNextIndex(index, syncConfig, collection);
            count++;
        }
    }

    protected abstract String getSql(long index, SyncConfig syncConfig);

    protected abstract long getNextIndex(long index, SyncConfig syncConfig, Collection<Map<String, Object>> collection);
}
