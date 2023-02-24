package com.hzh.dts.boot;

import com.hzh.dts.core.DTSDispatcher;
import com.hzh.dts.properties.DTSProperties;
import com.hzh.dts.core.adapter.OuterAdapterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: hzh
 * @Date: 2023/2/3 15:44
 */
@Configuration
@EnableConfigurationProperties(DTSProperties.class)
public class DtsAutoConfiguration {
    @Autowired
    private DTSProperties dtsProperties;

    @Bean(initMethod = "init")
    public OuterAdapterFactory outerAdapterFactory() {
        return new OuterAdapterFactory(dtsProperties);
    }

    @Bean
    public DTSDispatcher dtsDispatcher() {
        return new DTSDispatcher(outerAdapterFactory());
    }
}
