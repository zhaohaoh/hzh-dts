package com.hzh.dts.core.adapter;


import com.hzh.dts.properties.DTSProperties;
import com.hzh.dts.config.ResourceConfig;
import com.hzh.dts.config.SyncConfig;
import com.hzh.dts.constant.ResourceTypeEnum;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: hzh
 * @Date: 2023/1/29 9:54
 */
public class OuterAdapterFactory {

    protected DTSProperties dtsProperties;
    private static final Map<String, OuterAdapter> outerAdapterMap = new ConcurrentHashMap<>();

    public OuterAdapterFactory(DTSProperties dtsProperties) {
        this.dtsProperties = dtsProperties;
    }

    public void init() {
        outerAdapterMap.put(ResourceTypeEnum.DB.name(), new DbAdapter());
        outerAdapterMap.put(ResourceTypeEnum.ES.name(), new EsAdapter());
    }

    /**
     * 获取源
     *
     * @return {@link OuterAdapter}
     */
    public OuterAdapter getSource(SyncConfig syncConfig) {
        String dataSourceKey = syncConfig.getSourceDataSource();
        OuterAdapter outerAdapter = getOuterAdapter(dataSourceKey);
        return outerAdapter;
    }


    /**
     * 获取目标
     *
     * @return {@link OuterAdapter}
     */
    public OuterAdapter getTarget(SyncConfig syncConfig) {
        String dataSourceKey = syncConfig.getTargetDataSource();
        OuterAdapter outerAdapter = getOuterAdapter(dataSourceKey);
        return outerAdapter;
    }

    /**
     * 获取外适配器
     *
     * @param dataSourceKey 数据源关键
     * @return {@link OuterAdapter}
     */
    private OuterAdapter getOuterAdapter(String dataSourceKey) {
        String[] split = StringUtils.split(dataSourceKey,":");
        String type = split[0].toUpperCase();

        ResourceTypeEnum resourceTypeEnum = EnumUtils.getEnum(ResourceTypeEnum.class, type);
        if (resourceTypeEnum == null) {
            throw new RuntimeException("类型找不到");
        }
        ResourceConfig resourceConfig = null;
        switch (resourceTypeEnum) {
            case DB:
                resourceConfig = dtsProperties.getDbConfig().get(split[1]);
                break;
            case ES:
                resourceConfig = dtsProperties.getEsConfig().get(split[1]);
                break;
            default:
                break;
        }

        OuterAdapter outerAdapter = getAdapter(resourceConfig);
        return outerAdapter;
    }

    /**
     * 获取适配器
     *
     * @param datasourceInstance 数据源实例
     * @return {@link OuterAdapter}
     */
    private OuterAdapter getAdapter(ResourceConfig datasourceInstance) {
        ResourceTypeEnum type = datasourceInstance.getType();
        OuterAdapter outerAdapter = outerAdapterMap.get(type.name());
        outerAdapter.initClient(datasourceInstance);
        return outerAdapter;
    }
}
