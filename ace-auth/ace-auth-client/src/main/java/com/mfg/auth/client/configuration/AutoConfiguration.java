package com.mfg.auth.client.configuration;

import com.mfg.auth.client.config.ServiceAuthConfig;
import com.mfg.auth.client.config.UserAuthConfig;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mengfanguang on 2017/9/15.
 */
@Configuration
@ComponentScan({"com.mfg.auth.client","com.mfg.auth.common.event"})
@RemoteApplicationEventScan(basePackages = "com.mfg.auth.common.event")
public class AutoConfiguration {
    @Bean
    ServiceAuthConfig getServiceAuthConfig(){
        return new ServiceAuthConfig();
    }

    @Bean
    UserAuthConfig getUserAuthConfig(){
        return new UserAuthConfig();
    }

}
