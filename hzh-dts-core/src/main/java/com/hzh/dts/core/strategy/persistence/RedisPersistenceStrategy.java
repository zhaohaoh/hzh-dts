package com.hzh.dts.core.strategy.persistence;

import com.hzh.dts.util.JedisUtil;
import org.apache.commons.lang3.StringUtils;

public class RedisPersistenceStrategy implements PersistenceStrategy {

    @Override
    public void persistenceIndex(String taskId, String strategy, long index) {
        if (index <= 0) {
            return;
        }
        JedisUtil.setex(PREFIX + taskId + ":" + strategy, String.valueOf(index), 3600 * 72);
    }

    @Override
    public Long getPersistenceIndex(String taskId) {
        String index = JedisUtil.getValueByKey(PREFIX + taskId);
        if (StringUtils.isBlank(index)) {
            return null;
        }
        return Long.valueOf(index);
    }
}
