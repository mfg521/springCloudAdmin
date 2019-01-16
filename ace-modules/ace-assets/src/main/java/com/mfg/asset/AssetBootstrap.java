package com.mfg.asset;

import com.ace.cache.EnableAceCache;
import com.mfg.auth.client.EnableAceAuthClient;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author mengfanguang
 */

@EnableEurekaClient
@EnableSwagger2Doc
@SpringBootApplication
@EnableCircuitBreaker
@EnableFeignClients({"com.mfg.auth.client.feign"})
@EnableScheduling
@EnableAceAuthClient
@EnableAceCache
@EnableTransactionManagement
@MapperScan("com.mfg.asset.mapper")
public class AssetBootstrap {
    public static void main(String[] args) {
        new SpringApplicationBuilder(AssetBootstrap.class).web(true).run(args);
    }
}
