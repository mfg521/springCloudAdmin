package com.mfg.asset.config;

import com.mfg.auth.client.interceptor.ServiceAuthRestInterceptor;
import com.mfg.auth.client.interceptor.UserAuthRestInterceptor;
import com.mfg.auth.common.handler.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Collections;

@Configuration("assetWebConfig")
@Primary
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    GlobalExceptionHandler globalExceptionHandler(){return new GlobalExceptionHandler();}

    /**
     * 拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry){

        registry.addInterceptor(getServiceAuthRestInterceptor()).
                addPathPatterns(getIncludePathPatterns()).addPathPatterns("/api/user/validate");

        registry.addInterceptor(getUserAuthRestInterceptor()).
                addPathPatterns(getIncludePathPatterns());
    }

    @Bean
    ServiceAuthRestInterceptor getServiceAuthRestInterceptor(){return new ServiceAuthRestInterceptor();}

    @Bean
    UserAuthRestInterceptor getUserAuthRestInterceptor(){return new UserAuthRestInterceptor();}

    /**
     * 需要用户和服务认证判断的路径
     * @return
     */
    private ArrayList<String> getIncludePathPatterns(){
        ArrayList<String> list=new ArrayList<>();
        String [] urls={
                "/element/**",
                "/gateLog/**",
                "/group/**",
                "/groupType/**",
                "/menu/**",
                "/user/**",
                "/api/permissions",
                "/api/user/un/**"
        };
        Collections.addAll(list,urls);
        return list;
    }

}
