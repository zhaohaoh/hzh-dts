package com.hzh.dts.core.adapter;


import com.hzh.dts.config.ResourceConfig;
import com.hzh.dts.properties.TargetMapping;

import java.util.Collection;
import java.util.Map;

/**
 * 外部适配器接口
 */
public interface OuterAdapter {

    String getType(String type);
    /**
     * sql
     */
    void initClient(ResourceConfig datasourceInstance);
    /**
     * sql
     * @return
     */
      Collection<Map<String, Object>> select( String sql);


    void save(TargetMapping targetMapping, Collection<Map<String, Object>> objs);
}
