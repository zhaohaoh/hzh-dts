package com.hzh.dts.core.adapter;

import com.es.plus.client.EsPlusClientFacade;
import com.es.plus.core.statics.Es;
import com.es.plus.pojo.ClientContext;
import com.hzh.dts.config.EsConfig;
import com.hzh.dts.config.ResourceConfig;
import com.hzh.dts.constant.ResourceTypeEnum;
import com.hzh.dts.properties.TargetMapping;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: hzh
 * @Date: 2023/1/30 19:09
 */
public class EsAdapter extends AbstratAdapter {
    private EsPlusClientFacade esPlusClientFacade;

    @Override
    public String getType(String type) {
        return ResourceTypeEnum.ES.name();
    }

    @Override
    public Collection<Map<String, Object>> select(String sql) {
        return null;
    }

    @Override
    public void doSave(TargetMapping targetMapping, Collection<Map<String, Object>> objs) {
        List<Map> collect = objs.stream().map(a -> (Map) a).collect(Collectors.toList());
        Es.chainUpdate(esPlusClientFacade, Map.class).index(targetMapping.getTargetTable()).saveBatch(collect);
    }

    @Override
    public void initClient(ResourceConfig resourceConfig) {
        EsConfig config = (EsConfig) resourceConfig;
        // 处理地址
        String address = config.getAddress();
        if (StringUtils.isEmpty(address)) {
            throw new RuntimeException("please config the es address");
        }

        String schema = config.getSchema();
        List<HttpHost> hostList = new ArrayList<>();
        Arrays.stream(address.split(",")).forEach(item -> hostList.add(new HttpHost(item.split(":")[0],
                Integer.parseInt(item.split(":")[1]), schema)));

        // 转换成 HttpHost 数组
        HttpHost[] httpHost = hostList.toArray(new HttpHost[]{});
        // 构建连接对象
        RestClientBuilder builder = RestClient.builder(httpHost);

        // 设置账号密码最大连接数之类的
        String username = config.getUsername();
        String password = config.getPassword();
        Integer maxConnTotal = config.getMaxConnTotal();
        Integer maxConnPerRoute = config.getMaxConnPerRoute();
        Integer keepAliveMillis = config.getKeepAliveMillis();
        boolean needSetHttpClient = (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password))
                || (Objects.nonNull(maxConnTotal) || Objects.nonNull(maxConnPerRoute)) || Objects.nonNull(keepAliveMillis);
        if (needSetHttpClient) {
            builder.setHttpClientConfigCallback(httpClientBuilder -> {
                // 设置心跳时间等
                Optional.ofNullable(keepAliveMillis).ifPresent(p -> httpClientBuilder.setKeepAliveStrategy((response, context) -> p));
                Optional.ofNullable(maxConnTotal).ifPresent(httpClientBuilder::setMaxConnTotal);
                Optional.ofNullable(maxConnPerRoute).ifPresent(httpClientBuilder::setMaxConnPerRoute);
                if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
                    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    // 设置账号密码
                    credentialsProvider.setCredentials(AuthScope.ANY,
                            new UsernamePasswordCredentials(config.getUsername(), config.getPassword()));
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                }
                return httpClientBuilder;
            });
        }

        // 设置超时时间
        Integer connectTimeOut = config.getConnectTimeOut();
        Integer socketTimeOut = config.getSocketTimeOut();
        Integer connectionRequestTimeOut = config.getConnectionRequestTimeOut();
        boolean needSetRequestConfig = Objects.nonNull(connectTimeOut) || Objects.nonNull(connectionRequestTimeOut);
        if (needSetRequestConfig) {
            builder.setRequestConfigCallback(requestConfigBuilder -> {
                Optional.ofNullable(connectTimeOut).ifPresent(requestConfigBuilder::setConnectTimeout);
                Optional.ofNullable(socketTimeOut).ifPresent(requestConfigBuilder::setSocketTimeout);
                Optional.ofNullable(connectionRequestTimeOut)
                        .ifPresent(requestConfigBuilder::setConnectionRequestTimeout);
                return requestConfigBuilder;
            });
        }

        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(builder);

        esPlusClientFacade = ClientContext.buildEsPlusClientFacade(restHighLevelClient);
    }

}
