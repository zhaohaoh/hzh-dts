package com.hzh.dts;

import com.es.plus.starter.auto.EsAutoConfiguration;
import com.es.plus.starter.auto.EsClientConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {EsClientConfiguration.class, EsAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class SamplesApplication {
    public static void main(String[] args) {
        SpringApplication.run(SamplesApplication.class, args);
    }
}
