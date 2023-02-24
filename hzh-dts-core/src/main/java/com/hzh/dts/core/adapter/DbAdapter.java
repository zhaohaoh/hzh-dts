package com.hzh.dts.core.adapter;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.JdbcConstants;
import com.hzh.dts.config.DatasourceConfig;
import com.hzh.dts.config.ResourceConfig;
import com.hzh.dts.constant.ResourceTypeEnum;
import com.hzh.dts.properties.TargetMapping;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Author: hzh
 * @Date: 2023/1/29 10:27
 */
public class DbAdapter extends AbstratAdapter {
    private DataSource dataSource;

    private volatile JdbcTemplate jdbcTemplate = null;

    @Override
    public String getType(String type) {
        return ResourceTypeEnum.DB.name();
    }

    @Override
    public void initClient(ResourceConfig datasourceInstance) {
        DatasourceConfig config = (DatasourceConfig) datasourceInstance;
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(config.getDriver());
        ds.setUrl(config.getUrl());
        ds.setUsername(config.getUsername());
        ds.setPassword(config.getPassword());
        ds.setInitialSize(1);
        ds.setMinIdle(1);
        ds.setMaxActive(config.getMaxActive());
        ds.setMaxWait(60000);
        ds.setTimeBetweenEvictionRunsMillis(60000);
        ds.setMinEvictableIdleTimeMillis(300000);
        ds.setValidationQuery("select 1");
        ds.setTestOnBorrow(true);
        ds.setTestWhileIdle(true);
        dataSource = ds;
    }

    @Override
    public Collection<Map<String, Object>> select(String sql) {

        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        if (stmtList.size() != 1) {
            throw new RuntimeException("sql injection detected : " + sql);
        }

        JdbcTemplate jdbcTemplate = getJDbcTemplate(this.dataSource);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return maps;
    }

    @Override
    public void doSave(TargetMapping targetMapping, Collection<Map<String, Object>> objs) {

    }


    private JdbcTemplate getJDbcTemplate(DataSource dataSource) {
        if (jdbcTemplate == null) {
            synchronized (this) {
                if (jdbcTemplate == null) {
                    jdbcTemplate = new JdbcTemplate(dataSource);
                }
            }
        }
        return jdbcTemplate;
    }
}
